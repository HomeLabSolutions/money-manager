package com.d9tilov.moneymanager.standing.domain

import com.d9tilov.moneymanager.standing.data.entity.StandingData
import io.reactivex.Completable

interface StandingRepo {

    fun insert(standingData: StandingData): Completable
    fun update(standingData: StandingData): Completable
    fun delete(standingData: StandingData): Completable
}
