package com.d9tilov.moneymanager.goal.domain

import com.d9tilov.moneymanager.goal.domain.entity.Goal
import kotlinx.coroutines.flow.Flow

interface GoalInteractor {

    suspend fun insert(goal: Goal)
    fun getAll(): Flow<List<Goal>>
    suspend fun update(goal: Goal)
    suspend fun delete(goal: Goal)
}
