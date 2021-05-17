package com.d9tilov.moneymanager.goal.vm.dialog

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.RemoveGoalNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.goal.domain.GoalInteractor
import com.d9tilov.moneymanager.goal.domain.entity.Goal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoveGoalViewModel @Inject constructor(
    private val goalInteractor: GoalInteractor
) : BaseViewModel<RemoveGoalNavigator>() {

    fun remove(goal: Goal) = viewModelScope.launch {
        goalInteractor.delete(goal)
        navigator?.closeDialog()
    }
}
