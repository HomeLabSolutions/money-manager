package com.d9tilov.moneymanager.goal.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.CreatedGoalNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.goal.domain.GoalInteractor
import com.d9tilov.moneymanager.goal.domain.entity.Goal
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.math.BigDecimal

class CreatedGoalViewModel @AssistedInject constructor(
    private val goalInteractor: GoalInteractor,
    @Assisted val savedStateHandle: SavedStateHandle
) : BaseViewModel<CreatedGoalNavigator>() {

    fun save(name: String, sum: BigDecimal, description: String = "") = viewModelScope.launch {
        val goal = savedStateHandle.get<Goal>("goal")
        if (goal == null) {
            goalInteractor.insert(Goal(name = name, targetSum = sum, description = description))
        } else {
            goalInteractor.update(
                goal.copy(
                    name = name,
                    targetSum = sum,
                    description = description
                )
            )
        }
        navigator?.back()
    }
}
