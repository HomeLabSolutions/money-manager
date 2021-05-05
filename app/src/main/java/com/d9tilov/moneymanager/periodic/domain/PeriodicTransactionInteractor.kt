package com.d9tilov.moneymanager.periodic.domain

import com.d9tilov.moneymanager.periodic.domain.entity.PeriodicTransaction
import com.d9tilov.moneymanager.transaction.TransactionType
import io.reactivex.Completable
import io.reactivex.Flowable

interface PeriodicTransactionInteractor {

    fun insert(periodicTransactionData: PeriodicTransaction): Completable
    fun getAll(type: TransactionType): Flowable<List<PeriodicTransaction>>
    fun update(periodicTransactionData: PeriodicTransaction): Completable
    fun delete(periodicTransactionData: PeriodicTransaction): Completable
}
