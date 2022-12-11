package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagingData
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.android.core.utils.currentDateTime
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.core.utils.toLocal
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

interface TransactionRepo {

    suspend fun addTransaction(transaction: TransactionDataModel)
    fun getTransactionById(id: Long): Flow<TransactionDataModel>
    fun getTransactionsByType(
        from: LocalDateTime = Instant.fromEpochMilliseconds(0).toLocal(),
        to: LocalDateTime = currentDateTime().getStartOfDay(),
        transactionType: TransactionType
    ): Flow<PagingData<TransactionDataModel>>

    fun getTransactionsByTypeInPeriod(
        from: LocalDateTime = Instant.fromEpochMilliseconds(0).toLocal(),
        to: LocalDateTime = currentDateTime().getEndOfDay(),
        transactionType: TransactionType,
        onlyInStatistics: Boolean = true,
        withRegular: Boolean = true
    ): Flow<List<TransactionDataModel>>

    fun getByCategoryInPeriod(
        category: Category,
        from: LocalDateTime,
        to: LocalDateTime,
        inStatistics: Boolean
    ): Flow<List<TransactionDataModel>>

    suspend fun getAllByCategory(category: Category): List<TransactionDataModel>
    suspend fun getCountByCurrencyCode(code: String): Int
    suspend fun update(transaction: TransactionDataModel)
    suspend fun removeTransaction(transaction: TransactionDataModel)
    suspend fun clearAll()
}
