package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagingData
import com.d9tilov.moneymanager.base.domain.Interactor
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionChartModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

interface TransactionInteractor : Interactor {

    fun getTransactionsGroupedByCategory(
        type: TransactionType,
        from: LocalDateTime,
        to: LocalDateTime,
        currencyCode: String,
        inStatistics: Boolean,
        onlySubcategories: Boolean
    ): Flow<List<TransactionChartModel>>
    fun getTransactionById(id: Long): Flow<Transaction>
    fun getTransactionsByType(type: TransactionType): Flow<PagingData<Transaction>>
    fun ableToSpendToday(): Flow<BigDecimal>
    fun getSumInFiscalPeriodInUsd(type: TransactionType): Flow<BigDecimal>
    fun getSumInFiscalPeriod(): Flow<BigDecimal>
    fun getSumTodayInUsd(type: TransactionType): Flow<BigDecimal>
    fun getApproxSumInFiscalPeriodCurrentCurrency(type: TransactionType): Flow<BigDecimal>
    fun getApproxSumTodayCurrentCurrency(type: TransactionType): Flow<BigDecimal>
    fun removeAllByCategory(category: Category): Flow<Int>
    suspend fun addTransaction(transaction: Transaction)
    suspend fun getCurrentCurrencyCode(): String
    suspend fun executeRegularIfNeeded(type: TransactionType)
    suspend fun update(transaction: Transaction)
    suspend fun removeTransaction(transaction: Transaction)
    suspend fun clearAll()
}
