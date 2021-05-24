package com.d9tilov.moneymanager.goal.vm

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.GoalsNavigator
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.goal.domain.GoalInteractor
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val budgetInteractor: BudgetInteractor,
    private val userInteractor: UserInteractor,
    goalInteractor: GoalInteractor
) : BaseViewModel<GoalsNavigator>() {

    val budget = budgetInteractor.get().distinctUntilChanged().asLiveData()
    val goals = goalInteractor.getAll().asLiveData()

    fun savePrepopulateStatus() = viewModelScope.launch(Dispatchers.IO) {
        val user = userInteractor.getCurrentUser().first()
        userInteractor.updateUser(user.copy(showPrepopulate = false))
    }

    fun insertSaveSum(sum: BigDecimal) = viewModelScope.launch(Dispatchers.IO) {
        val budget = budgetInteractor.get().first()
        budgetInteractor.update(budget.copy(saveSum = sum))
    }
}
