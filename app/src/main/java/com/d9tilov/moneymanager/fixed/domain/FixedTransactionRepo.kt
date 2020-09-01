package com.d9tilov.moneymanager.fixed.domain

import com.d9tilov.moneymanager.fixed.data.entity.FixedTransactionData
import io.reactivex.Completable

interface FixedTransactionRepo {

    fun insert(fixedTransactionData: FixedTransactionData): Completable
    fun update(fixedTransactionData: FixedTransactionData): Completable
    fun delete(fixedTransactionData: FixedTransactionData): Completable
}
