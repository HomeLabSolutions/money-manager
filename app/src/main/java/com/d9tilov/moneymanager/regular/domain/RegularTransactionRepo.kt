package com.d9tilov.moneymanager.regular.domain

import com.d9tilov.moneymanager.regular.data.entity.RegularTransactionData
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.coroutines.flow.Flow

interface RegularTransactionRepo {

    suspend fun insert(regularTransactionData: RegularTransactionData)
    fun getAll(type: TransactionType): Flow<List<RegularTransactionData>>
    suspend fun update(regularTransactionData: RegularTransactionData)
    suspend fun delete(regularTransactionData: RegularTransactionData)
}
