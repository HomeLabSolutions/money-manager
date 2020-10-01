package com.d9tilov.moneymanager.goal.domain

import com.d9tilov.moneymanager.goal.domain.entity.Goal
import io.reactivex.Completable
import io.reactivex.Flowable

interface GoalInteractor {

    fun insert(goal: Goal): Completable
    fun getAll(): Flowable<List<Goal>>
    fun update(goal: Goal): Completable
    fun delete(goal: Goal): Completable
}
