package com.d9tilov.moneymanager.goal.vm

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.GoalsNavigator
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.getFirstDayOfMonth
import com.d9tilov.moneymanager.goal.domain.GoalInteractor
import com.d9tilov.moneymanager.goal.domain.entity.Goal
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date

class GoalsViewModel @ViewModelInject constructor(
    private val budgetInteractor: BudgetInteractor,
    private val userInteractor: UserInteractor,
    goalInteractor: GoalInteractor
) : BaseViewModel<GoalsNavigator>() {

    private val amount = MutableLiveData<BigDecimal>()
    lateinit var goals: LiveData<List<Goal>>

    init {
        viewModelScope.launch {
            goals = goalInteractor.getAll().asLiveData()
            val count = budgetInteractor.getCount()
            val budgetData: BudgetData
            if (count == 0) {
                budgetData = BudgetData(
                    sum = BigDecimal.ZERO,
                    fiscalDay = Date().getFirstDayOfMonth()
                )
                budgetInteractor.create(budgetData)
                amount.value = budgetData.sum
            } else {
                budgetData = budgetInteractor.get()
            }
            amount.value = budgetData.sum
        }
    }

    fun savePrepopulateStatus() = viewModelScope.launch {
        userInteractor.getCurrentUser()
            .map { userInteractor.updateUser(it.copy(showPrepopulate = false)) }
    }

    fun getAmount(): LiveData<BigDecimal> = amount
}
