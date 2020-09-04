package com.d9tilov.moneymanager.fixed.domain

import com.d9tilov.moneymanager.fixed.data.entity.FixedTransactionData
import com.d9tilov.moneymanager.transaction.TransactionType
import io.reactivex.Completable
import io.reactivex.Flowable

interface FixedTransactionRepo {

    fun insert(fixedTransactionData: FixedTransactionData): Completable
    fun getAll(type: TransactionType): Flowable<List<FixedTransactionData>>
    fun update(fixedTransactionData: FixedTransactionData): Completable
    fun delete(fixedTransactionData: FixedTransactionData): Completable
}
