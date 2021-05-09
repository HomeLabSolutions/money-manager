package com.d9tilov.moneymanager.transaction.data.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.category.data.entity.Category
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Date

class TransactionLocalSource(
    private val preferencesStore: PreferencesStore,
    appDatabase: AppDatabase,
    private val transactionDataMapper: TransactionDataMapper,
    private val transactionDateDataMapper: TransactionDateDataMapper
) : TransactionSource {

    private val transactionDao: TransactionDao = appDatabase.transactionDao()

    override suspend fun insert(transaction: TransactionDataModel) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val count = transactionDao.getDateItemsCountInDay(
                currentUserId,
                transaction.type,
                transaction.date
            )
            when {
                count > 1 -> {
                    throw TransactionCreateException("Can't create transaction. DataBase contains more than one date item in day period")
                }
                count == 1 -> {
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
                            transaction.date,
                            transaction.currency
                        )
                    )
                    transactionDao.insert(
                        transactionDataMapper.toDbModel(
                            transaction.copy(
                                clientId = currentUserId
                            )
                        )
                    )
                }
            }
        }
    }

    private fun createDateItem(
        clientId: String,
        type: TransactionType,
        date: Date,
        currency: String
    ): TransactionDbModel {
        return transactionDateDataMapper.toDbModel(
            TransactionDateDataModel(
                clientId = clientId,
                type = type,
                date = date.getEndOfDay(),
                currency = currency
            )
        )
    }

    override fun getById(id: Long): Flow<TransactionDataModel> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.getById(currentUserId, id).map { transactionDataMapper.toDataModel(it) }
        }
    }

    override fun getAllByType(
        from: Date,
        to: Date,
        transactionType: TransactionType
    ): Flow<PagingData<TransactionBaseDataModel>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            Pager(
                config = PagingConfig(
                    PAGE_SIZE,
                    INITIAL_PAGE_SIZE,
                    false
                )
            ) { transactionDao.getAllByType(currentUserId, from, to, transactionType) }.flow
                .map { value ->
                    value.map {
                        if (it.isDate) {
                            transactionDateDataMapper.toDataModel(it)
                        } else {
                            transactionDataMapper.toDataModel(it)
                        }
                    }
                }
        }
    }

    override suspend fun update(transaction: TransactionDataModel) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.getById(currentUserId, transaction.id)
                .map { oldTransaction ->
                    {
                        GlobalScope.launch(Dispatchers.IO) {
                            if (oldTransaction.date.isSameDay(transaction.date)) {
                                transactionDao.update(
                                    transactionDataMapper.toDbModel(
                                        transaction
                                    )
                                )
                            } else {
                                val itemsCountOldTransactionDate =
                                    transactionDao.getItemsCountInDay(
                                        currentUserId,
                                        oldTransaction.type,
                                        oldTransaction.date
                                    )
                                if (itemsCountOldTransactionDate == 0) {
                                    throw IllegalArgumentException("Date count must be more than 0")
                                }
                                if (itemsCountOldTransactionDate == 1) {
                                    transactionDao.deleteDate(
                                        currentUserId,
                                        oldTransaction.type,
                                        oldTransaction.date
                                    )
                                }
                                val itemsCountNewTransactionDate =
                                    transactionDao.getItemsCountInDay(
                                        currentUserId,
                                        transaction.type,
                                        transaction.date
                                    )
                                if (itemsCountNewTransactionDate == 0) {
                                    transactionDao.insert(
                                        createDateItem(
                                            currentUserId,
                                            transaction.type,
                                            transaction.date,
                                            transaction.currency
                                        )
                                    )
                                    transactionDao.update(
                                        transactionDataMapper.toDbModel(transaction)
                                    )
                                } else {
                                    transactionDao.update(
                                        transactionDataMapper.toDbModel(transaction)
                                    )
                                }
                            }
                        }
                    }
                }
        }
    }

    override suspend fun remove(transaction: TransactionDataModel) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val count = transactionDao.getItemsCountInDay(
                currentUserId,
                transaction.type,
                transaction.date
            )
            when {
                count > 1 -> {
                    transactionDao.delete(
                        currentUserId,
                        transactionDataMapper.toDbModel(transaction).id
                    )
                }
                count == 1 -> {
                    transactionDao.delete(
                        currentUserId,
                        transactionDataMapper.toDbModel(transaction).id
                    )
                    transactionDao.deleteDate(
                        currentUserId,
                        transaction.type,
                        transaction.date
                    )
                }
                else -> {
                    throw TransactionCreateException("Can't create transaction. DataBase contains zero date item in day period")
                }
            }
        }
    }

    override suspend fun removeAllByCategory(category: Category) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.getByCategoryId(currentUserId, category.id)
                .map { list ->
                    {
                        GlobalScope.launch(Dispatchers.IO) {
                            for (transaction in list) {
                                val count = transactionDao.getItemsCountInDay(
                                    currentUserId,
                                    transaction.type,
                                    transaction.date
                                )
                                when {
                                    count > 1 -> {
                                        transactionDao.deleteByCategoryId(
                                            currentUserId,
                                            category.id
                                        )
                                    }
                                    count == 1 -> {
                                        transactionDao.deleteByCategoryId(
                                            currentUserId,
                                            category.id
                                        )
                                        transactionDao.deleteDate(
                                            currentUserId,
                                            transaction.type,
                                            transaction.date
                                        )
                                    }
                                    else -> {
                                        throw TransactionCreateException("Can't create transaction. DataBase contains zero date item in day period")
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    override suspend fun clearAll() {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.clearAll(currentUserId)
        }
    }

    companion object {
        private const val PAGE_SIZE = 30
        private const val INITIAL_PAGE_SIZE = 3 * PAGE_SIZE
    }
}
