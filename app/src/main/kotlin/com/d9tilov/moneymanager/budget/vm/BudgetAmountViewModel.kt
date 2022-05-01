package com.d9tilov.moneymanager.budget.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.BudgetAmountNavigator
import com.d9tilov.moneymanager.budget.domain.entity.BudgetData
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class BudgetAmountViewModel @Inject constructor(private val budgetInteractor: BudgetInteractor) :
    BaseViewModel<BudgetAmountNavigator>() {

    val budgetData = budgetInteractor.get()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, BudgetData.EMPTY)

    init {
        viewModelScope.launch(Dispatchers.IO) { budgetInteractor.create() }
    }

    fun saveBudgetAmount(sum: BigDecimal) = viewModelScope.launch(Dispatchers.IO) {
        val budget = budgetInteractor.get().first()
        budgetInteractor.update(budget.copy(sum = sum))
    }
}
