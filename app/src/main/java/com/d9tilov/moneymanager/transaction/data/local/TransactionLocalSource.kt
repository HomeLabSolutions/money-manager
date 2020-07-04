package com.d9tilov.moneymanager.transaction.data.local

import androidx.paging.DataSource
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
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
import io.reactivex.Observable
import io.reactivex.Single
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
            transactionDao.ableToInsertDate(currentUserId, transaction.type, transaction.date)
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
                            ).andThen(
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
        return transactionDateDataMapper.toDbModel(TransactionDateDataModel(clientId, type, date))
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

    override fun getAllByType2(transactionType: TransactionType): Single<List<TransactionBaseDataModel>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.getAllByType2(currentUserId, transactionType)
                .flatMap {
                    Observable.fromIterable(it)
                        .map { transactionDbModel ->
                            if (transactionDbModel.isDate) {
                                transactionDateDataMapper.toDataModel(transactionDbModel)
                            } else {
                                transactionDataMapper.toDataModel(transactionDbModel)
                            }
                        }.toList()
                }
        }
    }

    override fun remove(transaction: TransactionBaseDataModel): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            transactionDao.delete(
                currentUserId,
                transactionDataMapper.toDbModel(transaction as TransactionDataModel).id
            )
        }
    }
}
