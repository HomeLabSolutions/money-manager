package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.DataSource
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionBaseDataModel
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import io.reactivex.Completable
import io.reactivex.Flowable
import java.util.Date

interface TransactionRepo {

    fun addTransaction(transaction: TransactionDataModel): Completable
    fun getTransactionById(id: Long): Flowable<TransactionDataModel>
    fun getTransactionsByType(
        from: Date = Date(0),
        to: Date = Date(),
        transactionType: TransactionType
    ): DataSource.Factory<Int, TransactionBaseDataModel>

    fun removeTransaction(transaction: TransactionDataModel): Completable

    fun clearAll(): Completable
}
