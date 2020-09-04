package com.d9tilov.moneymanager.fixed.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.fixed.data.entity.FixedTransactionData
import com.d9tilov.moneymanager.transaction.TransactionType
import io.reactivex.Completable
import io.reactivex.Flowable

interface FixedTransactionSource : Source {

    fun insert(fixedTransactionData: FixedTransactionData): Completable
    fun getAll(type: TransactionType): Flowable<List<FixedTransactionData>>
    fun update(fixedTransactionData: FixedTransactionData): Completable
    fun delete(fixedTransactionData: FixedTransactionData): Completable
}
