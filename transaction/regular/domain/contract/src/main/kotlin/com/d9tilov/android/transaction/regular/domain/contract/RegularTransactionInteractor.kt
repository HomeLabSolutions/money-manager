package com.d9tilov.android.transaction.regular.domain.contract

import com.d9tilov.android.category.domain.model.Category
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.regular.domain.model.RegularTransaction
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
