package com.d9tilov.android.transaction.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.core.utils.toLocal
import com.d9tilov.android.database.dao.TransactionDao
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.transaction.data.contract.TransactionSource
import com.d9tilov.android.transaction.data.impl.mapper.toDataModel
import com.d9tilov.android.transaction.data.impl.mapper.toDbModel
import com.d9tilov.android.transaction.data.model.TransactionDataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

class TransactionLocalSource(
    private val preferencesStore: PreferencesStore,
    private val transactionDao: TransactionDao
) : TransactionSource {

    override suspend fun insert(transaction: TransactionDataModel) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else transactionDao.insert(transaction.copy(clientId = currentUserId).toDbModel())
    }

    override fun getById(id: Long): Flow<TransactionDataModel> =
        preferencesStore.uid
            .filterNotNull()
            .flatMapMerge { uid -> transactionDao.getById(uid, id).map { it.toDataModel() } }

    override fun getAllByTypePaging(
        transactionType: TransactionType
    ): Flow<PagingData<TransactionDataModel>> = preferencesStore.uid
        .filterNotNull()
        .flatMapMerge { uid ->
            Pager(config = PagingConfig(PAGE_SIZE, enablePlaceholders = false)) {
                transactionDao.getAllByType(
                    uid,
                    Instant.fromEpochMilliseconds(0).toLocal().getEndOfDay(),
                    currentDateTime().getEndOfDay(),
                    transactionType.value
                )
            }.flow.map { value -> value.map { it.toDataModel() } }
        }

    override fun getAllByTypeInPeriod(
        from: LocalDateTime,
        to: LocalDateTime,
        transactionType: TransactionType,
        onlyInStatistics: Boolean,
        withRegular: Boolean
    ): Flow<List<TransactionDataModel>> = preferencesStore.uid
        .filterNotNull()
        .flatMapMerge { uid ->
            transactionDao.getAllByTypeInPeriod(
                uid,
                from.getStartOfDay(),
                to.getEndOfDay(),
                transactionType.value
            )
                .map { list ->
                    list.map { item -> item.toDataModel() }
                        .filter { if (onlyInStatistics) it.inStatistics else true }
                        .filter { if (!withRegular) !it.isRegular else true }
                }
        }

    override suspend fun getAllByCategory(category: Category): List<TransactionDataModel> {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return if (currentUserId == null) throw WrongUidException()
        else transactionDao.getByCategoryId(currentUserId, category.id)
            .map { item -> item.toDataModel() }
    }

    override fun getByCategoryInPeriod(
        category: Category,
        from: LocalDateTime,
        to: LocalDateTime,
        onlyInStatistics: Boolean
    ): Flow<List<TransactionDataModel>> {
        return preferencesStore.uid
            .filterNotNull()
            .flatMapMerge { uid ->
                transactionDao.getByCategoryIdInPeriod(uid, category.id, from, to)
                    .map { list ->
                        list.map { item -> item.toDataModel() }
                            .filter { if (onlyInStatistics) it.inStatistics else true }
                    }
            }
    }

    override suspend fun getCountByCurrencyCode(code: String): Int {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return if (currentUserId == null) throw WrongUidException()
        else transactionDao.getCountByCurrencyCode(code, currentUserId)
    }

    override suspend fun update(transaction: TransactionDataModel) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else transactionDao.update(transaction.toDbModel())
    }

    override suspend fun remove(transaction: TransactionDataModel) {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else transactionDao.delete(currentUserId, transaction.toDbModel().id)
    }

    override suspend fun clearAll() {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else transactionDao.clearAll(currentUserId)
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
