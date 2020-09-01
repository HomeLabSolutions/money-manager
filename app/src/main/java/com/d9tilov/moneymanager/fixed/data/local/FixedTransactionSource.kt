package com.d9tilov.moneymanager.fixed.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.fixed.data.entity.FixedTransactionData
import io.reactivex.Completable

interface FixedTransactionSource : Source {

    fun insert(fixedTransactionData: FixedTransactionData): Completable
    fun update(fixedTransactionData: FixedTransactionData): Completable
    fun delete(fixedTransactionData: FixedTransactionData): Completable
}
