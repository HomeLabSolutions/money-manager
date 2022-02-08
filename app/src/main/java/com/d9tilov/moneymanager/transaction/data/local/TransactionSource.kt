package com.d9tilov.moneymanager.transaction.data.local

import androidx.paging.PagingData
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface TransactionSource {

    suspend fun insert(transaction: TransactionDataModel)
    fun getById(id: Long): Flow<TransactionDataModel>
    fun getAllByTypePaging(
        from: LocalDateTime,
        to: LocalDateTime,
        transactionType: TransactionType
    ): Flow<PagingData<TransactionDataModel>>

    fun getAllByTypeInPeriod(
        from: LocalDateTime,
        to: LocalDateTime,
        transactionType: TransactionType,
        onlyInStatistics: Boolean,
        withRegular: Boolean
    ): Flow<List<TransactionDataModel>>

    fun getByCategory(category: Category): Flow<List<TransactionDataModel>>
    suspend fun getCountByCurrencyCode(code: String): Int
    suspend fun update(transaction: TransactionDataModel)
    suspend fun remove(transaction: TransactionDataModel)
    fun removeAllByCategory(category: Category): Flow<Int>
    suspend fun clearAll()
}
