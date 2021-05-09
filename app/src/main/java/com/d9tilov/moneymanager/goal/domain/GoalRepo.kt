package com.d9tilov.moneymanager.goal.domain

import com.d9tilov.moneymanager.goal.data.entity.GoalData
import kotlinx.coroutines.flow.Flow

interface GoalRepo {

    suspend fun insert(goalData: GoalData)
    fun getAll(): Flow<List<GoalData>>
    suspend fun update(goalData: GoalData)
    suspend fun delete(goalData: GoalData)
}
