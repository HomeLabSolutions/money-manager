package com.d9tilov.moneymanager.transaction.domain

import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.data.entity.TransactionDataModel
import io.reactivex.Completable
import io.reactivex.Flowable

interface TransactionRepo {

    fun addTransaction(transaction: TransactionDataModel): Completable
    fun getTransactionById(id: Long): Flowable<TransactionDataModel>
    fun getTransactionsByType(transactionType: TransactionType): Flowable<List<TransactionDataModel>>
    fun removeTransaction(transaction: TransactionDataModel): Completable
}
