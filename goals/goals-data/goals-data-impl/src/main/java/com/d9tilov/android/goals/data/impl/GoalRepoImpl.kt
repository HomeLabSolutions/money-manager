package com.d9tilov.android.goals.data.impl

import com.d9tilov.android.goals.data.contract.GoalSource
import com.d9tilov.android.goals.data.model.GoalData
import kotlinx.coroutines.flow.Flow

class GoalRepoImpl(private val goalSource: GoalSource) :
    com.d9tilov.android.goals.data.contract.GoalRepo {

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
