package com.d9tilov.moneymanager.budget.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.BudgetAmountNavigator
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigDecimal

class BudgetAmountViewModel @ViewModelInject constructor(private val budgetInteractor: BudgetInteractor) :
    BaseViewModel<BudgetAmountNavigator>() {

    val budgetData = budgetInteractor.get().asLiveData()

    fun saveBudgetAmount(sum: BigDecimal) = viewModelScope.launch {
        budgetInteractor.get().collect { budgetInteractor.update(it.copy(sum = sum)) }
    }
}
