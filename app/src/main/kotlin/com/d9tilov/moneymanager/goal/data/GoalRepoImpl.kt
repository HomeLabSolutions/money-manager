package com.d9tilov.moneymanager.goal.data

import com.d9tilov.moneymanager.goal.data.entity.GoalData
import com.d9tilov.moneymanager.goal.data.local.GoalSource
import com.d9tilov.moneymanager.goal.domain.GoalRepo
import kotlinx.coroutines.flow.Flow

class GoalRepoImpl(private val goalSource: GoalSource) : GoalRepo {

    override suspend fun insert(goalData: GoalData) {
        goalSource.insert(goalData)
    }

    override fun getAll(): Flow<List<GoalData>> = goalSource.getAll()

    override suspend fun update(goalData: GoalData) {
        goalSource.update(goalData)
    }

    override suspend fun delete(goalData: GoalData) {
        goalSource.delete(goalData)
    }
}
