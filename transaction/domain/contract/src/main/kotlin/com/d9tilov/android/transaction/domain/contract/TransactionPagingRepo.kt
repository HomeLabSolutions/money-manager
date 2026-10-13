package com.d9tilov.android.transaction.domain.contract

import androidx.paging.PagingData
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import kotlinx.coroutines.flow.Flow

interface TransactionPagingRepo {
    fun getTransactionsByType(transactionType: TransactionType): Flow<PagingData<TransactionDataModel>>

    fun searchTransactionsByType(
        transactionType: TransactionType,
        query: String,
    ): Flow<PagingData<TransactionDataModel>>
}
