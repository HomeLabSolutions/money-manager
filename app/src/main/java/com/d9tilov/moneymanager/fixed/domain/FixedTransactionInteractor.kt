package com.d9tilov.moneymanager.fixed.domain

import com.d9tilov.moneymanager.fixed.domain.entity.FixedTransaction
import com.d9tilov.moneymanager.transaction.TransactionType
import io.reactivex.Completable
import io.reactivex.Flowable

interface FixedTransactionInteractor {

    fun insert(fixedTransactionData: FixedTransaction): Completable
    fun getAll(type: TransactionType): Flowable<List<FixedTransaction>>
    fun update(fixedTransactionData: FixedTransaction): Completable
    fun delete(fixedTransactionData: FixedTransaction): Completable
}
