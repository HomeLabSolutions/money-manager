package com.d9tilov.moneymanager.transaction.data.local

import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import io.reactivex.Completable
import io.reactivex.Flowable

interface TransactionSource {

    fun insert(transaction: TransactionDataModel): Completable
    fun getById(id: Long): Flowable<TransactionDataModel>
    fun getByType(transactionType: TransactionType): Flowable<List<TransactionDataModel>>
    fun remove(transaction: TransactionDataModel): Completable
}
