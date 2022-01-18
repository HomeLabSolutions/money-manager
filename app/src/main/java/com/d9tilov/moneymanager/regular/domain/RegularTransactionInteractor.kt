package com.d9tilov.moneymanager.regular.domain

import com.d9tilov.moneymanager.regular.domain.entity.RegularTransaction
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.coroutines.flow.Flow

interface RegularTransactionInteractor {

    fun createDefault(): Flow<RegularTransaction>
    fun getAll(type: TransactionType): Flow<List<RegularTransaction>>
    suspend fun getById(id: Long): RegularTransaction
    suspend fun insert(regularTransactionData: RegularTransaction)
    suspend fun update(regularTransactionData: RegularTransaction)
    suspend fun delete(regularTransactionData: RegularTransaction)
}
