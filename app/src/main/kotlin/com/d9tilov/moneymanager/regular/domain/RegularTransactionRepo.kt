package com.d9tilov.moneymanager.regular.domain

import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.regular.data.entity.RegularTransactionData
import com.d9tilov.android.database.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface RegularTransactionRepo {

    fun getAll(type: TransactionType): Flow<List<RegularTransactionData>>
    suspend fun getAllByCategory(category: Category): List<RegularTransactionData>
    suspend fun getById(id: Long): RegularTransactionData
    suspend fun insert(regularTransactionData: RegularTransactionData)
    suspend fun update(regularTransactionData: RegularTransactionData)
    suspend fun delete(regularTransactionData: RegularTransactionData)
}
