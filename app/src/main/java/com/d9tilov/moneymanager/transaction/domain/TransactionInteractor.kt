package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagingData
import com.d9tilov.moneymanager.base.domain.Interactor
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.util.Date

interface TransactionInteractor : Interactor {

    suspend fun addTransaction(transaction: Transaction)
    fun getTransactionById(id: Long): Flow<Transaction>
    fun getTransactionsByType(type: TransactionType): Flow<PagingData<BaseTransaction>>
    fun getTransactionsByTypeWithoutDates(
        from: Date,
        to: Date,
        type: TransactionType
    ): Flow<List<Transaction>>

    fun getSumInFiscalPeriodInUsd(type: TransactionType): Flow<BigDecimal>
    fun getApproxSumInFiscalPeriodCurrentCurrency(type: TransactionType): Flow<BigDecimal>
    suspend fun update(transaction: Transaction)
    suspend fun removeTransaction(transaction: Transaction)
    fun removeAllByCategory(category: Category): Flow<Int>
    suspend fun clearAll()
}
