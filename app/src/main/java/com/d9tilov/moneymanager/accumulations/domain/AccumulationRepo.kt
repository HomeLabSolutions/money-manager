package com.d9tilov.moneymanager.accumulations.domain

import com.d9tilov.moneymanager.accumulations.data.entity.Accumulation
import io.reactivex.Completable

interface AccumulationRepo {

    fun insert(accumulation: Accumulation): Completable
    fun update(accumulation: Accumulation): Completable
}
