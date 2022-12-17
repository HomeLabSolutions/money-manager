package com.d9tilov.moneymanager.regular.domain


import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.android.core.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface RegularTransactionInteractor {

    fun createDefault(): Flow<RegularTransaction>
    fun getAll(type: TransactionType): Flow<List<RegularTransaction>>
    suspend fun removeAllByCategory(category: Category)
    suspend fun getById(id: Long): RegularTransaction
    suspend fun insert(regularTransactionData: RegularTransaction)
    suspend fun update(regularTransactionData: RegularTransaction)
    suspend fun delete(regularTransactionData: RegularTransaction)
}
