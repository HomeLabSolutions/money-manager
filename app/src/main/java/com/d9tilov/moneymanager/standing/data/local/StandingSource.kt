package com.d9tilov.moneymanager.standing.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.standing.data.entity.StandingData
import io.reactivex.Completable

interface StandingSource : Source {

    fun insert(standingData: StandingData): Completable
    fun update(standingData: StandingData): Completable
    fun delete(standingData: StandingData): Completable
}
