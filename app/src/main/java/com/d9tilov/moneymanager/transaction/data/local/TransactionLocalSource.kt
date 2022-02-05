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
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import com.d9tilov.moneymanager.transaction.data.mapper.toDataModel
import com.d9tilov.moneymanager.transaction.data.mapper.toDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime

class TransactionLocalSource(
    private val preferencesStore: PreferencesStore,
    private val transactionDao: TransactionDao
) : TransactionSource {

    override suspend fun insert(transaction: TransactionDataModel) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.insert(transaction.copy(clientId = currentUserId).toDbModel())
        }
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
        from: LocalDateTime,
        to: LocalDateTime,
        transactionType: TransactionType
    ): Flow<PagingData<TransactionDataModel>> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            Pager(config = PagingConfig(PAGE_SIZE, enablePlaceholders = false)) {
                transactionDao.getAllByType(
                    currentUserId,
                    from.getEndOfDay(),
                    to.getEndOfDay(),
                    transactionType
                )
            }.flow.map { value -> value.map { it.toDataModel() } }
        }
    }

    override fun getAllByTypeWithoutDates(
        from: LocalDateTime,
        to: LocalDateTime,
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
                transactionDao.update(transaction.toDbModel())
            }
        }
    }

    override suspend fun remove(transaction: TransactionDataModel) {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else transactionDao.delete(currentUserId, transaction.toDbModel().id)
    }

    override fun removeAllByCategory(category: Category): Flow<Int> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.getByCategoryId(currentUserId, category.id)
                .map { list ->
                    for (transaction in list) {
                        transactionDao.deleteByCategoryId(currentUserId, category.id)
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
