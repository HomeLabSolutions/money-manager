package com.d9tilov.moneymanager.goal.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.d9tilov.moneymanager.base.ui.navigator.CreatedGoalNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.goal.domain.GoalInteractor
import com.d9tilov.moneymanager.goal.domain.entity.Goal
import java.math.BigDecimal

class CreatedGoalViewModel @ViewModelInject constructor(
    private val goalInteractor: GoalInteractor,
    @Assisted val savedStateHandle: SavedStateHandle
) :
    BaseViewModel<CreatedGoalNavigator>() {

    fun save(name: String, sum: BigDecimal, description: String = "") {
        val goal = savedStateHandle.get<Goal>("goal")
        if (goal == null) {
            goalInteractor.insert(Goal(name = name, targetSum = sum, description = description))
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { navigator?.back() }
                .addTo(compositeDisposable)
        } else {
            goalInteractor.update(goal)
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
                .subscribe { navigator?.back() }
                .addTo(compositeDisposable)
        }
    }
}