package com.d9tilov.moneymanager.budget.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.BudgetAmountNavigator
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.getFirstDayOfMonth
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date

class BudgetAmountViewModel @ViewModelInject constructor(private val budgetInteractor: BudgetInteractor) :
    BaseViewModel<BudgetAmountNavigator>() {

    private val budgetData by lazy { MutableLiveData<BudgetData>() }

    init {
        viewModelScope.launch {
            val count = budgetInteractor.getCount()
            val budget: BudgetData
            if (count == 0) {
                budget = BudgetData(
                    sum = BigDecimal.ZERO,
                    fiscalDay = Date().getFirstDayOfMonth()
                )
                budgetInteractor.create(budget)
            } else {
                budget = budgetInteractor.get()
            }
            budgetData.value = budget
        }
    }

    fun saveBudgetAmount(sum: BigDecimal) = viewModelScope.launch {
        val newBudget = budgetInteractor.get().copy(sum = sum)
        budgetInteractor.update(newBudget)
        budgetData.value = newBudget
    }

    fun getAmount(): LiveData<BudgetData> = budgetData
}
