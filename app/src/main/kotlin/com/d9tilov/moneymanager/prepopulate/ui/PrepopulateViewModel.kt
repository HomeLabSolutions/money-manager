package com.d9tilov.moneymanager.prepopulate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.budget.vm.BudgetUiState
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.currency.domain.UpdateCurrencyInteractor
import com.d9tilov.moneymanager.currency.vm.CurrencyUiState
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class PrepopulateUiState(
    val currencyUiState: CurrencyUiState = CurrencyUiState.NoCurrencies(),
    val budgetUiState: BudgetUiState = BudgetUiState()
)

@HiltViewModel
class PrepopulateViewModel @Inject constructor(
    private val currencyInteractor: CurrencyInteractor,
    private val updateCurrencyInteractor: UpdateCurrencyInteractor,
    private val budgetInteractor: BudgetInteractor,
    private val userInteractor: UserInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrepopulateUiState())
    val uiState: StateFlow<PrepopulateUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            budgetInteractor.create()
            combine(
                currencyInteractor.getCurrencies(),
                budgetInteractor.get()
            ) { currencyList, budget ->
                PrepopulateUiState(
                    CurrencyUiState.HasCurrencies(currencyList, false),
                    BudgetUiState(budget)
                )
            }.collect { state -> _uiState.update { state } }
        }
    }

    fun changeCurrency(currencyCode: String) = viewModelScope.launch(Dispatchers.IO) {
        updateCurrencyInteractor.updateCurrency(currencyCode)
    }

    fun changeBudgetAmount(amount: String) {
        _uiState.update {
            val budgetUiState = it.budgetUiState
            val budgetData =
                budgetUiState.budgetData.copy(sum = amount.toBigDecimalOrNull() ?: BigDecimal.ZERO)
            val newBudgetUiState = budgetUiState.copy(budgetData = budgetData)
            it.copy(budgetUiState = newBudgetUiState)
        }
    }

    fun saveBudgetAmountAndComplete() = viewModelScope.launch(Dispatchers.IO) {
        launch {
            val budget = budgetInteractor.get().firstOrNull()
            budget?.let { budgetInteractor.update(budget.copy(sum = _uiState.value.budgetUiState.budgetData.sum)) }
        }
        launch { userInteractor.prepopulateCompleted() }
    }
}
