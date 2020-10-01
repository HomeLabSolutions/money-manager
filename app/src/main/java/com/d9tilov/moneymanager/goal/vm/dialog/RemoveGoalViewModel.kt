package com.d9tilov.moneymanager.goal.vm.dialog

import androidx.hilt.lifecycle.ViewModelInject
import com.d9tilov.moneymanager.base.ui.navigator.RemoveGoalNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.goal.data.entity.GoalData
import com.d9tilov.moneymanager.goal.domain.GoalInteractor
import com.d9tilov.moneymanager.goal.domain.entity.Goal

class RemoveGoalViewModel @ViewModelInject constructor(
    private val goalInteractor: GoalInteractor
) : BaseViewModel<RemoveGoalNavigator>() {

    fun remove(goal: Goal) {
        goalInteractor.delete(goal)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { navigator?.closeDialog() }
            .addTo(compositeDisposable)
    }
}
