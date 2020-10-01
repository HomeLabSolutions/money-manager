package com.d9tilov.moneymanager.goal.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.goal.data.entity.GoalData
import io.reactivex.Completable
import io.reactivex.Flowable

interface GoalSource : Source {

    fun insert(goalData: GoalData): Completable
    fun getAll(): Flowable<List<GoalData>>
    fun update(goalData: GoalData): Completable
    fun delete(goalData: GoalData): Completable
}
