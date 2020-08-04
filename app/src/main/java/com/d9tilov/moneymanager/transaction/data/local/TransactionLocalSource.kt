package com.d9tilov.moneymanager.transaction.data.local

import androidx.paging.DataSource
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.core.util.getEndOfDay
import com.d9tilov.moneymanager.core.util.isSameDay
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionBaseDataModel
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDateDataModel
import com.d9tilov.moneymanager.transaction.data.local.entity.TransactionDbModel
import com.d9tilov.moneymanager.transaction.data.mapper.TransactionDataMapper
import com.d9tilov.moneymanager.transaction.data.mapper.TransactionDateDataMapper
import com.d9tilov.moneymanager.transaction.exception.TransactionCreateException
import io.reactivex.Completable
import io.reactivex.Flowable
import java.util.Date

class TransactionLocalSource(
    private val preferencesStore: PreferencesStore,
    appDatabase: AppDatabase,
    private val transactionDataMapper: TransactionDataMapper,
    private val transactionDateDataMapper: TransactionDateDataMapper
) : TransactionSource {

    private val transactionDao: TransactionDao = appDatabase.transactionDao()

    override fun insert(transaction: TransactionDataModel): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            transactionDao.getDateItemsCountInDay(
                currentUserId,
                transaction.type,
                transaction.date
            )
                .flatMapCompletable {
                    when {
                        it > 1 -> {
                            Completable.error(TransactionCreateException("Can't create transaction. DataBase contains more than one date item in day period"))
                        }
                        it > 0 -> {
                            transactionDao.insert(
                                transactionDataMapper.toDbModel(
                                    transaction.copy(
                                        clientId = currentUserId
                                    )
                                )
                            )
                        }
                        else -> {
                            transactionDao.insert(
                                createDateItem(
                                    currentUserId,
                                    transaction.type,
                                    transaction.date
                                )
                            )
                                .andThen(
                                    transactionDao.insert(
                                        transactionDataMapper.toDbModel(
                                            transaction.copy(
                                                clientId = currentUserId
                                            )
                                        )
                                    )
                                )
                        }
                    }
                }
        }
    }

    private fun createDateItem(
        clientId: String,
        type: TransactionType,
        date: Date
    ): TransactionDbModel {
        return transactionDateDataMapper.toDbModel(
            TransactionDateDataModel(
                clientId = clientId,
                type = type,
                date = date.getEndOfDay()
            )
        )
    }

    override fun getById(id: Long): Flowable<TransactionDataModel> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Flowable.error(WrongUidException())
        } else {
            transactionDao.getById(currentUserId, id).map { transactionDataMapper.toDataModel(it) }
        }
    }

    override fun getAllByType(
        from: Date,
        to: Date,
        transactionType: TransactionType
    ): DataSource.Factory<Int, TransactionBaseDataModel> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.getAllByType(currentUserId, from, to, transactionType)
                .map { transactionDbModel ->
                    if (transactionDbModel.isDate) {
                        transactionDateDataMapper.toDataModel(transactionDbModel)
                    } else {
                        transactionDataMapper.toDataModel(transactionDbModel)
                    }
                }
        }
    }

    override fun update(transaction: TransactionDataModel): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            transactionDao.getById(currentUserId, transaction.id)
                .firstOrError()
                .flatMapCompletable { oldTransaction ->
                    if (oldTransaction.date.isSameDay(transaction.date)) {
                        transactionDao.update(transactionDataMapper.toDbModel(transaction))
                    } else {
                        val itemsCountOldTransactionDateSingle = transactionDao.getItemsCountInDay(
                            currentUserId,
                            oldTransaction.type,
                            oldTransaction.date
                        )
                        val itemsCountNewTransactionDateSingle = transactionDao.getItemsCountInDay(
                            currentUserId,
                            transaction.type,
                            transaction.date
                        )
                        itemsCountOldTransactionDateSingle.flatMapCompletable { count ->
                            when (count) {
                                0 -> {
                                    Completable.error(IllegalArgumentException("Date count must be more than 0"))
                                }
                                1 -> {
                                    transactionDao.deleteDate(
                                        currentUserId,
                                        oldTransaction.type,
                                        oldTransaction.date
                                    )
                                }
                                else -> {
                                    Completable.complete()
                                }
                            }
                        }
                            .andThen(
                                itemsCountNewTransactionDateSingle.flatMapCompletable { count ->
                                    if (count == 0) {
                                        transactionDao.insert(
                                            createDateItem(
                                                currentUserId,
                                                transaction.type,
                                                transaction.date
                                            )
                                        ).andThen(
                                            transactionDao.update(
                                                transactionDataMapper.toDbModel(transaction)
                                            )
                                        )
                                    } else {
                                        transactionDao.update(
                                            transactionDataMapper.toDbModel(transaction)
                                        )
                                    }
                                }
                            )
                    }
                }
        }
    }

    override fun remove(transaction: TransactionDataModel): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            transactionDao.getItemsCountInDay(
                currentUserId,
                transaction.type,
                transaction.date
            ).flatMapCompletable {
                when {
                    it > 1 -> {
                        transactionDao.delete(
                            currentUserId,
                            transactionDataMapper.toDbModel(transaction).id
                        )
                    }
                    it == 1 -> {
                        transactionDao.delete(
                            currentUserId,
                            transactionDataMapper.toDbModel(transaction).id
                        ).andThen(
                            transactionDao.deleteDate(
                                currentUserId,
                                transaction.type,
                                transaction.date
                            )
                        )
                    }
                    else -> {
                        Completable.error(TransactionCreateException("Can't create transaction. DataBase contains zero date item in day period"))
                    }
                }
            }
        }
    }

    override fun clearAll(): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            transactionDao.clearAll(currentUserId)
        }
    }
}