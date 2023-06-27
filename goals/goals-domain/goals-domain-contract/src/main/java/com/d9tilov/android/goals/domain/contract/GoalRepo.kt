package com.d9tilov.android.goals.domain.contract

import com.d9tilov.android.goals.domain.model.GoalData
import kotlinx.coroutines.flow.Flow

interface GoalRepo {

    suspend fun insert(goalData: GoalData)
    fun getAll(): Flow<List<GoalData>>
    suspend fun update(goalData: GoalData)
    suspend fun delete(goalData: GoalData)
}
