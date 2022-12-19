package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagingData

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.android.database.model.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionChartModel
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionLineChartModel
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionSpendingTodayModel
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
    fun getSumInFiscalPeriodInUsd(type: TransactionType): Flow<BigDecimal>
    fun getSumInFiscalPeriod(): Flow<BigDecimal>
    fun getSumTodayInUsd(type: TransactionType): Flow<BigDecimal>
    fun getApproxSumInFiscalPeriodCurrentCurrency(type: TransactionType): Flow<BigDecimal>
    fun getApproxSumTodayCurrentCurrency(type: TransactionType): Flow<BigDecimal>
    suspend fun removeAllByCategory(category: Category)
    suspend fun addTransaction(transaction: Transaction)
    suspend fun executeRegularIfNeeded(type: TransactionType)
    suspend fun update(transaction: Transaction)
    suspend fun removeTransaction(transaction: Transaction)
    suspend fun clearAll()
}
