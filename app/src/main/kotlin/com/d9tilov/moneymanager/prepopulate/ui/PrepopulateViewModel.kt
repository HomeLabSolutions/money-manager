package com.d9tilov.moneymanager.prepopulate.ui

import com.d9tilov.moneymanager.base.ui.navigator.GoalsNavigator
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.budget.domain.entity.BudgetData
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class PrepopulateUiState(
    val currencyList: List<DomainCurrency> = emptyList(),
    val budgetData: BudgetData = BudgetData.EMPTY,
    val loading: Boolean = false
)

@HiltViewModel
class PrepopulateViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val budgetInteractor: BudgetInteractor
) : BaseViewModel<GoalsNavigator>() {

    private val _uiState = MutableStateFlow(PrepopulateUiState(loading = true))
    val uiState: StateFlow<PrepopulateUiState> = _uiState.asStateFlow()

}
