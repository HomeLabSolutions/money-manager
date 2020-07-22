package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagedList
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import io.reactivex.Completable
import io.reactivex.Flowable

interface TransactionInteractor {

    fun addTransaction(transaction: Transaction): Completable
    fun getTransactionById(id: Long): Flowable<Transaction>
    fun getTransactionsByType(type: TransactionType): Flowable<PagedList<BaseTransaction>>
    fun removeTransaction(transaction: Transaction): Completable
    fun clearAll(): Completable
}
