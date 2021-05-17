package com.d9tilov.moneymanager.budget.vm

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.BudgetAmountNavigator
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class BudgetAmountViewModel @Inject constructor(private val budgetInteractor: BudgetInteractor) :
    BaseViewModel<BudgetAmountNavigator>() {

    val budgetData = budgetInteractor.get().asLiveData()

    fun saveBudgetAmount(sum: BigDecimal) = viewModelScope.launch {
        budgetInteractor.get().collect { budgetInteractor.update(it.copy(sum = sum)) }
    }
}
