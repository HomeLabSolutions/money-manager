package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagingData
import com.d9tilov.moneymanager.base.domain.Interactor
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionInteractor : Interactor {

    suspend fun addTransaction(transaction: Transaction)
    fun getTransactionById(id: Long): Flow<Transaction>
    fun getTransactionsByType(type: TransactionType): Flow<PagingData<BaseTransaction>>
    suspend fun update(transaction: Transaction)
    suspend fun removeTransaction(transaction: Transaction)
    suspend fun removeAllByCategory(category: Category)
    suspend fun clearAll()
}
