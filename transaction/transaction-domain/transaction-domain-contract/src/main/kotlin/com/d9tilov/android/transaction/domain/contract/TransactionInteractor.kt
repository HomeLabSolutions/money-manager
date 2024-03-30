package com.d9tilov.android.transaction.domain.contract

import androidx.paging.PagingData
import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.domain.model.Transaction
import com.d9tilov.android.transaction.domain.model.TransactionChartModel
import com.d9tilov.android.transaction.domain.model.TransactionLineChartModel
import com.d9tilov.android.transaction.domain.model.TransactionSpendingTodayModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

interface TransactionInteractor {

    fun getTransactionsGroupedByCategory(
        type: TransactionType,
        from: LocalDateTime,
        to: LocalDateTime,
        currencyCode: String,
        inStatistics: Boolean,
        onlySubcategories: Boolean
    ): Flow<List<TransactionChartModel>>

    fun getTransactionsGroupedByDate(
        type: TransactionType,
        from: LocalDateTime,
        to: LocalDateTime,
        currencyCode: String,
        inStatistics: Boolean
    ): Flow<Map<LocalDateTime, TransactionLineChartModel>>

    suspend fun getTransactionsByCategory(
        type: TransactionType,
        category: Category,
        from: LocalDateTime,
        to: LocalDateTime,
        inStatistics: Boolean
    ): List<Transaction>

    fun getTransactionById(id: Long): Flow<Transaction>
    fun getTransactionsByType(type: TransactionType): Flow<PagingData<Transaction>>
    fun ableToSpendToday(): Flow<TransactionSpendingTodayModel>
    fun ableToSpendInFiscalPeriod(): Flow<BigDecimal>
    fun isSpendInPeriodApprox(type: TransactionType): Flow<Boolean>
    fun getSumInFiscalPeriod(): Flow<BigDecimal>
    fun isSpendTodayApprox(type: TransactionType): Flow<Boolean>
    fun getApproxSumInFiscalPeriodCurrentCurrency(type: TransactionType): Flow<BigDecimal>
    fun getApproxSumTodayCurrentCurrency(type: TransactionType): Flow<BigDecimal>
    suspend fun removeAllByCategory(category: Category)
    suspend fun addTransaction(transaction: Transaction)
    suspend fun executeRegularIfNeeded(type: TransactionType)
    suspend fun update(transaction: Transaction)
    suspend fun removeTransaction(transaction: Transaction)
    suspend fun clearAll()
}
