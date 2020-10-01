package com.d9tilov.moneymanager.goal.data

import com.d9tilov.moneymanager.goal.data.entity.GoalData
import com.d9tilov.moneymanager.goal.data.local.GoalSource
import com.d9tilov.moneymanager.goal.domain.GoalRepo
import io.reactivex.Completable
import io.reactivex.Flowable

class GoalRepoImpl(private val goalSource: GoalSource) : GoalRepo {

    override fun insert(goalData: GoalData) = goalSource.insert(goalData)

    override fun getAll(): Flowable<List<GoalData>> = goalSource.getAll()

    override fun update(goalData: GoalData): Completable = goalSource.update(goalData)

    override fun delete(goalData: GoalData): Completable = goalSource.delete(goalData)
}
