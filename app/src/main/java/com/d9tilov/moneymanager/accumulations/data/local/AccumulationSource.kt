package com.d9tilov.moneymanager.accumulations.data.local

import com.d9tilov.moneymanager.accumulations.data.entity.Accumulation
import io.reactivex.Completable

interface AccumulationSource {

    fun insert(accumulation: Accumulation): Completable
    fun update(accumulation: Accumulation): Completable
}
