package com.d9tilov.android.goals.domain.contract

import com.d9tilov.android.goals.domain.model.Goal

interface GoalInteractor {

    suspend fun insert(goal: Goal)
    suspend fun update(goal: Goal)
    suspend fun delete(goal: Goal)
}
