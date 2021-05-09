package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagingData
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionBaseDataModel
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface TransactionRepo {

    suspend fun addTransaction(transaction: TransactionDataModel)
    fun getTransactionById(id: Long): Flow<TransactionDataModel>
    fun getTransactionsByType(
        from: Date = Date(0),
        to: Date = Date(),
        transactionType: TransactionType
    ): Flow<PagingData<TransactionBaseDataModel>>

    suspend fun update(transaction: TransactionDataModel)
    suspend fun removeTransaction(transaction: TransactionDataModel)
    suspend fun removeAllByCategory(category: Category)
    suspend fun clearAll()
}
