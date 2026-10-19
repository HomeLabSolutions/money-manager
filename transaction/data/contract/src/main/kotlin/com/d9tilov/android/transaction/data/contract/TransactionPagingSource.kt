package com.d9tilov.android.transaction.data.contract

import androidx.paging.PagingData
import com.d9tilov.android.core.model.TransactionType
import com.d9tilov.android.transaction.domain.model.TransactionDataModel
import kotlinx.coroutines.flow.Flow

interface TransactionPagingSource {
    fun getAllByTypePaging(transactionType: TransactionType): Flow<PagingData<TransactionDataModel>>

    fun searchByTypePaging(
        transactionType: TransactionType,
        query: String,
    ): Flow<PagingData<TransactionDataModel>>
}
