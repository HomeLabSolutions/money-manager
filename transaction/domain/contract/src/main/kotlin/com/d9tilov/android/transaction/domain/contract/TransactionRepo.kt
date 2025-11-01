package com.d9tilov.android.transaction.domain.contract

import androidx.paging.PagingData
import com.d9tilov.android.category.domain.entity.Category
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import com.d9tilov.android.transaction.domain.model.TransactionMinMaxDateModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface TransactionRepo {
    suspend fun addTransaction(transaction: TransactionDataModel)

    fun getTransactionById(id: Long): Flow<TransactionDataModel>

    fun getTransactionsByType(transactionType: TransactionType): Flow<PagingData<TransactionDataModel>>

    fun getTransactionsByTypeInPeriod(
        from: LocalDateTime,
        to: LocalDateTime,
        transactionType: TransactionType,
        onlyInStatistics: Boolean = true,
        withRegular: Boolean = true,
    ): Flow<List<TransactionDataModel>>

    fun getByCategoryInPeriod(
        category: Category,
        from: LocalDateTime,
        to: LocalDateTime,
        inStatistics: Boolean,
    ): Flow<List<TransactionDataModel>>

    suspend fun getAllByCategory(category: Category): List<TransactionDataModel>

    suspend fun getTransactionMinMaxDate(): TransactionMinMaxDateModel

    suspend fun update(transaction: TransactionDataModel)

    suspend fun removeTransaction(transaction: TransactionDataModel)

    suspend fun clearAll()
}
