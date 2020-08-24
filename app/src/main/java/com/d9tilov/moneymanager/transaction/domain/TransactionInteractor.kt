package com.d9tilov.moneymanager.transaction.domain

import androidx.paging.PagedList
import com.d9tilov.moneymanager.base.domain.Interactor
import com.d9tilov.moneymanager.category.data.entity.Category
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.entity.BaseTransaction
import com.d9tilov.moneymanager.transaction.domain.entity.Transaction
import io.reactivex.Completable
import io.reactivex.Flowable

interface TransactionInteractor : Interactor {

    fun addTransaction(transaction: Transaction): Completable
    fun getTransactionById(id: Long): Flowable<Transaction>
    fun getTransactionsByType(type: TransactionType): Flowable<PagedList<BaseTransaction>>
    fun update(transaction: Transaction): Completable
    fun removeTransaction(transaction: Transaction): Completable
    fun removeAllByCategory(category: Category): Completable
    fun clearAll(): Completable
}
