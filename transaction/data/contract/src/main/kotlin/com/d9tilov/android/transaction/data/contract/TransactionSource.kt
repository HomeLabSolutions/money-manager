package com.d9tilov.android.transaction.data.contract

import androidx.paging.PagingData
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface TransactionSource {
    suspend fun insert(transaction: TransactionDataModel)

    fun getById(id: Long): Flow<TransactionDataModel>

    fun getAllByTypePaging(transactionType: TransactionType): Flow<PagingData<TransactionDataModel>>

    fun getAllByTypeInPeriod(
        from: LocalDateTime,
        to: LocalDateTime,
        transactionType: TransactionType,
        onlyInStatistics: Boolean,
        withRegular: Boolean,
    ): Flow<List<TransactionDataModel>>

    fun getByCategoryInPeriod(
        category: Category,
        from: LocalDateTime,
        to: LocalDateTime,
        onlyInStatistics: Boolean,
    ): Flow<List<TransactionDataModel>>

    suspend fun getAllByCategory(category: Category): List<TransactionDataModel>

    suspend fun getCountByCurrencyCode(code: String): Int

    suspend fun update(transaction: TransactionDataModel)

    suspend fun remove(transaction: TransactionDataModel)

    suspend fun clearAll()
}
