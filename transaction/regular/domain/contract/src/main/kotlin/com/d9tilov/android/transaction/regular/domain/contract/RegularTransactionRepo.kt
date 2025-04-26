package com.d9tilov.android.transaction.regular.domain.contract

import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.regular.domain.model.RegularTransactionData
import kotlinx.coroutines.flow.Flow

interface RegularTransactionRepo {
    fun getAll(type: TransactionType): Flow<List<RegularTransactionData>>

    suspend fun getAllByCategory(category: Category): List<RegularTransactionData>

    suspend fun getById(id: Long): RegularTransactionData

    suspend fun insert(regularTransactionData: RegularTransactionData)

    suspend fun update(regularTransactionData: RegularTransactionData)

    suspend fun delete(regularTransactionData: RegularTransactionData)
}
