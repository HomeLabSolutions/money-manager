package com.d9tilov.moneymanager.transaction.data.local

import androidx.paging.PagingData
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionBaseDataModel
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface TransactionSource {

    suspend fun insert(transaction: TransactionDataModel)
    fun getById(id: Long): Flow<TransactionDataModel>
    fun getAllByType(
        from: Date,
        to: Date,
        transactionType: TransactionType
    ): Flow<PagingData<TransactionBaseDataModel>>

    fun getAllByTypeWithoutDates(
        from: Date,
        to: Date,
        transactionType: TransactionType
    ): Flow<List<TransactionDataModel>>

    fun getByCategory(category: Category): Flow<List<TransactionDataModel>>
    suspend fun getCountByCurrencyCode(code: String): Int
    suspend fun update(transaction: TransactionDataModel)
    suspend fun remove(transaction: TransactionDataModel)
    suspend fun removeAllByCategory(category: Category)
    suspend fun clearAll()
}
