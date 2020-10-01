package com.d9tilov.moneymanager.goal.domain

import com.d9tilov.moneymanager.goal.data.entity.GoalData
import io.reactivex.Completable
import io.reactivex.Flowable

interface GoalRepo {

    fun insert(goalData: GoalData): Completable
    fun getAll(): Flowable<List<GoalData>>
    fun update(goalData: GoalData): Completable
    fun delete(goalData: GoalData): Completable
}
