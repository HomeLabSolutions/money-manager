package com.d9tilov.moneymanager.transaction.data.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.core.util.getEndOfDay
import com.d9tilov.moneymanager.core.util.getStartOfDay
import com.d9tilov.moneymanager.core.util.isSameDay
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionBaseDataModel
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDateDataModel
import com.d9tilov.moneymanager.transaction.data.local.entity.TransactionDbModel
import com.d9tilov.moneymanager.transaction.data.mapper.toDataDateModel
import com.d9tilov.moneymanager.transaction.data.mapper.toDataModel
import com.d9tilov.moneymanager.transaction.data.mapper.toDbModel
import com.d9tilov.moneymanager.transaction.exception.TransactionCreateException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Date

class TransactionLocalSource(
    private val preferencesStore: PreferencesStore,
    private val transactionDao: TransactionDao
) : TransactionSource {

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
                        transaction.copy(
                            clientId = currentUserId
                        ).toDbModel()
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
                        transaction.copy(clientId = currentUserId).toDbModel()
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
        return TransactionDateDataModel(
            clientId = clientId,
            type = type,
            date = date.getEndOfDay(),
            currency = currency
        ).toDbModel()
    }

    override fun getById(id: Long): Flow<TransactionDataModel> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.getById(currentUserId, id).map { it.toDataModel() }
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
                    enablePlaceholders = false
                )
            ) {
                transactionDao.getAllByType(
                    currentUserId,
                    from.getEndOfDay(),
                    to.getEndOfDay(),
                    transactionType
                )
            }.flow
                .map { value ->
                    value.map {
                        when (it.isDate) {
                            true -> it.toDataDateModel()
                            false -> it.toDataModel()
                        }
                    }
                }
        }
    }

    override fun getAllByTypeWithoutDates(
        from: Date,
        to: Date,
        transactionType: TransactionType,
        onlyInStatistics: Boolean
    ): Flow<List<TransactionDataModel>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.getAllByTypeInPeriod(
                currentUserId,
                from.getStartOfDay(),
                to.getEndOfDay(),
                transactionType
            )
                .map { list ->
                    list.map { item -> item.toDataModel() }
                        .filter { if (onlyInStatistics) it.inStatistics else true }
                }
        }
    }

    override fun getByCategory(category: Category): Flow<List<TransactionDataModel>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.getByCategoryId(currentUserId, category.id)
                .map { list -> list.map { item -> item.toDataModel() } }
        }
    }

    override suspend fun getCountByCurrencyCode(code: String): Int {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.getCountByCurrencyCode(code, currentUserId)
        }
    }

    override suspend fun update(transaction: TransactionDataModel) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val oldTransaction = transactionDao.getById(currentUserId, transaction.id).first()
            if (oldTransaction.date.isSameDay(transaction.date)) {
                transactionDao.update(transaction.toDbModel())
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
                        oldTransaction.date.getStartOfDay(),
                        oldTransaction.date.getEndOfDay()
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
                }
                transactionDao.update(transaction.toDbModel())
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
                count > 1 -> transactionDao.delete(
                    currentUserId,
                    transaction.toDbModel().id
                )
                count == 1 -> {
                    transactionDao.delete(
                        currentUserId,
                        transaction.toDbModel().id
                    )
                    transactionDao.deleteDate(
                        currentUserId,
                        transaction.type,
                        transaction.date.getStartOfDay(),
                        transaction.date.getEndOfDay()
                    )
                }
                else -> throw TransactionCreateException("Can't create transaction. DataBase contains zero date item in day period")
            }
        }
    }

    override fun removeAllByCategory(category: Category): Flow<Int> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.getByCategoryId(currentUserId, category.id)
                .map { list ->
                    for (transaction in list) {
                        val count = transactionDao.getItemsCountInDay(
                            currentUserId,
                            transaction.type,
                            transaction.date
                        )
                        when {
                            count > 1 -> transactionDao.deleteByCategoryId(
                                currentUserId,
                                category.id
                            )
                            count == 1 -> {
                                transactionDao.deleteByCategoryId(
                                    currentUserId,
                                    category.id
                                )
                                transactionDao.deleteDate(
                                    currentUserId,
                                    transaction.type,
                                    transaction.date.getStartOfDay(),
                                    transaction.date.getEndOfDay()
                                )
                            }
                            else -> throw TransactionCreateException("Can't create transaction. DataBase contains zero date item in day period")
                        }
                    }
                    list.size
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
        private const val PAGE_SIZE = 10
    }
}
