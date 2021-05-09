package com.d9tilov.moneymanager.regular.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.regular.data.entity.RegularTransactionData
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.coroutines.flow.Flow

interface RegularTransactionSource : Source {

    suspend fun insert(regularTransactionData: RegularTransactionData)
    fun getAll(type: TransactionType): Flow<List<RegularTransactionData>>
    suspend fun update(regularTransactionData: RegularTransactionData)
    suspend fun delete(regularTransactionData: RegularTransactionData)
}
