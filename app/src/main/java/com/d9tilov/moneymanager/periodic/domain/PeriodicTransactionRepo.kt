package com.d9tilov.moneymanager.periodic.domain

import com.d9tilov.moneymanager.periodic.data.entity.PeriodicTransactionData
import com.d9tilov.moneymanager.transaction.TransactionType
import io.reactivex.Completable
import io.reactivex.Flowable

interface PeriodicTransactionRepo {

    fun insert(periodicTransactionData: PeriodicTransactionData): Completable
    fun getAll(type: TransactionType): Flowable<List<PeriodicTransactionData>>
    fun update(periodicTransactionData: PeriodicTransactionData): Completable
    fun delete(periodicTransactionData: PeriodicTransactionData): Completable
}
