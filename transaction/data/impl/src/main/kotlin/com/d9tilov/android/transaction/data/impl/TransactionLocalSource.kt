package com.d9tilov.android.transaction.data.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.core.exceptions.WrongUidException
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.core.utils.toLocal
import com.d9tilov.android.database.dao.TransactionDao
import com.d9tilov.android.database.dao.TransactionSearchQuery
import com.d9tilov.android.database.entity.TransactionDbModel
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.transaction.data.contract.TransactionSource
import com.d9tilov.android.transaction.data.impl.mapper.toDataModel
import com.d9tilov.android.transaction.data.impl.mapper.toDbModel
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import com.d9tilov.android.transaction.domain.model.TransactionMinMaxDateModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDateTime
import javax.inject.Inject
import javax.inject.Named
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class TransactionLocalSource @Inject constructor(
    @param:Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
    private val preferencesStore: PreferencesStore,
    private val transactionDao: TransactionDao,
) : TransactionSource {
    override suspend fun insert(transaction: TransactionDataModel) {
        val currentUserId = preferencesStore.uid.firstOrNull()
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.insert(transaction.copy(clientId = currentUserId).toDbModel())
        }
    }

    override fun getById(id: Long): Flow<TransactionDataModel> =
        preferencesStore.uid
            .filterNotNull()
            .flatMapMerge { uid -> transactionDao.getById(uid, id).map { it.toDataModel() } }

    override fun getAllByTypePaging(transactionType: TransactionType): Flow<PagingData<TransactionDataModel>> =
        pagedTransactions { uid, from, to ->
            transactionDao.getAllByType(uid, from, to, transactionType.value)
        }

    override fun searchByTypePaging(
        transactionType: TransactionType,
        query: String,
    ): Flow<PagingData<TransactionDataModel>> {
        val searchQuery = toTransactionSearchQuery(query)
        return pagedTransactions { uid, from, to ->
            transactionDao.searchByTypeQuery(
                clientId = uid,
                from = from,
                to = to,
                type = transactionType.value,
                textPattern = searchQuery.textPattern,
                amountQuery = searchQuery.amountQuery,
            )
        }
    }

    private fun toTransactionSearchQuery(query: String): TransactionSearchQuery {
        val amountQuery = toAmountSearchQuery(query)
        return TransactionSearchQuery(
            amountQuery = amountQuery,
            textPattern = toTextSearchPattern(query.trim()),
        )
    }

    private fun toAmountSearchQuery(query: String): String? =
        query
            .replace(',', '.')
            .filterNot { it.isWhitespace() }
            .toBigDecimalOrNull()
            ?.stripTrailingZeros()
            ?.toPlainString()

    // SQLite LIKE only ignores ASCII case. Character classes also match Cyrillic letters.
    private fun toTextSearchPattern(query: String): String =
        query
            .map { character ->
                when (character) {
                    '*' -> {
                        "[*]"
                    }

                    '?' -> {
                        "[?]"
                    }

                    '[' -> {
                        "[[]"
                    }

                    else -> {
                        if (character.lowercaseChar() != character.uppercaseChar()) {
                            "[$character${character.uppercaseChar()}${character.lowercaseChar()}]"
                        } else {
                            character.toString()
                        }
                    }
                }
            }.joinToString(separator = "", prefix = "*", postfix = "*")

    @OptIn(ExperimentalTime::class)
    private fun pagedTransactions(
        pagingSourceFactory: (String, LocalDateTime, LocalDateTime) -> PagingSource<Int, TransactionDbModel>,
    ): Flow<PagingData<TransactionDataModel>> =
        preferencesStore.uid.filterNotNull().flatMapMerge { uid ->
            Pager(config = PagingConfig(PAGE_SIZE, enablePlaceholders = false)) {
                pagingSourceFactory(
                    uid,
                    Instant.fromEpochMilliseconds(0).toLocal().getEndOfDay(),
                    currentDateTime().getEndOfDay(),
                )
            }.flow
                .map { value -> value.map { it.toDataModel() } }
                .flowOn(ioDispatcher)
        }

    override fun getAllByTypeInPeriod(
        from: LocalDateTime,
        to: LocalDateTime,
        transactionType: TransactionType,
        onlyInStatistics: Boolean,
        withRegular: Boolean,
    ): Flow<List<TransactionDataModel>> =
        preferencesStore.uid.filterNotNull().flatMapMerge { uid ->
            transactionDao
                .getAllByTypeInPeriod(
                    uid,
                    from.getStartOfDay(),
                    to.getEndOfDay(),
                    transactionType.value,
                    onlyInStatistics,
                    withRegular,
                ).map { list -> list.map { item -> item.toDataModel() } }
        }

    override suspend fun getAllByCategory(category: Category): List<TransactionDataModel> {
        val currentUserId = preferencesStore.uid.firstOrNull()
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.getByCategoryId(currentUserId, category.id).map { item -> item.toDataModel() }
        }
    }

    override fun getByCategoryInPeriod(
        category: Category,
        from: LocalDateTime,
        to: LocalDateTime,
        onlyInStatistics: Boolean,
    ): Flow<List<TransactionDataModel>> =
        preferencesStore.uid.filterNotNull().flatMapMerge { uid ->
            transactionDao.getByCategoryIdInPeriod(uid, category.id, from, to, onlyInStatistics).map { list ->
                list.map { item -> item.toDataModel() }
            }
        }

    override suspend fun getMinMaxDate(): TransactionMinMaxDateModel {
        val currentUserId = preferencesStore.uid.firstOrNull()
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.getMinMaxDate(currentUserId).toDataModel()
        }
    }

    override suspend fun update(transaction: TransactionDataModel) {
        val currentUserId = preferencesStore.uid.firstOrNull()
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.update(transaction.toDbModel())
        }
    }

    override suspend fun remove(transaction: TransactionDataModel) {
        val currentUserId = preferencesStore.uid.firstOrNull()
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            transactionDao.delete(currentUserId, transaction.id)
        }
    }

    override suspend fun clearAll() {
        val currentUserId = preferencesStore.uid.firstOrNull()
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
