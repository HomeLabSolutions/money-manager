package com.d9tilov.moneymanager.goal.vm

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.d9tilov.moneymanager.base.ui.navigator.CreatedGoalNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.goal.domain.GoalInteractor
import com.d9tilov.moneymanager.goal.domain.entity.Goal
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
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

    @AssistedFactory
    interface CreatedGoalViewModelFactory {
        fun create(handle: SavedStateHandle): CreatedGoalViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: CreatedGoalViewModelFactory,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel?> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return assistedFactory.create(handle) as T
                }
            }
    }
}
