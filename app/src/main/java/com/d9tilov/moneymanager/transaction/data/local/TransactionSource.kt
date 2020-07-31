package com.d9tilov.moneymanager.transaction.data.local

import androidx.paging.DataSource
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionBaseDataModel
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import io.reactivex.Completable
import io.reactivex.Flowable
import java.util.Date

interface TransactionSource {

    fun insert(transaction: TransactionDataModel): Completable
    fun getById(id: Long): Flowable<TransactionDataModel>
    fun getAllByType(
        from: Date,
        to: Date,
        transactionType: TransactionType
    ): DataSource.Factory<Int, TransactionBaseDataModel>

    fun update(transaction: TransactionDataModel): Completable
    fun remove(transaction: TransactionDataModel): Completable
    fun clearAll(): Completable
}
