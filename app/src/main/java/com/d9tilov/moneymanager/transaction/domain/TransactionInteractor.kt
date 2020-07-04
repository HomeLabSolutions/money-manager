package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagedList
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface TransactionInteractor {

    fun addTransaction(transaction: Transaction): Completable
    fun getTransactionById(id: Long): Flowable<Transaction>
    fun getTransactionsByType(type: TransactionType): Flowable<PagedList<BaseTransaction>>
    fun getTransactionsByType2(type: TransactionType): Single<List<BaseTransaction>>
    fun removeTransaction(transaction: BaseTransaction): Completable
}
