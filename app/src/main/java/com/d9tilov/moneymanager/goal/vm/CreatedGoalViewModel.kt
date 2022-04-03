package com.d9tilov.moneymanager.goal.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.CreatedGoalNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.goal.domain.GoalInteractor
import com.d9tilov.moneymanager.goal.domain.entity.Goal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CreatedGoalViewModel @Inject constructor(
    private val goalInteractor: GoalInteractor,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel<CreatedGoalNavigator>() {

    fun save(name: String, sum: BigDecimal, description: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
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
        }
        navigator?.back()
    }
}
