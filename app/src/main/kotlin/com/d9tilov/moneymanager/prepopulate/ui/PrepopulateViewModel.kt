package com.d9tilov.moneymanager.prepopulate.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.budget.vm.BudgetUiState
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.currency.vm.CurrencyUiState
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
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
    currencyInteractor: CurrencyInteractor,
    private val userInteractor: UserInteractor,
    private val budgetInteractor: BudgetInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrepopulateUiState())
    val uiState: StateFlow<PrepopulateUiState> = combine(
        currencyInteractor.getCurrencies(),
        budgetInteractor.get()
    ) { currencyList, budget ->
        PrepopulateUiState(
            CurrencyUiState.HasCurrencies(currencyList, false),
            BudgetUiState(budget)
        )
    }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = PrepopulateUiState()
        )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            budgetInteractor.create()
        }
    }

    fun changeCurrency(currencyCode: String) = viewModelScope.launch(Dispatchers.IO) {
        launch {
            userInteractor.updateCurrency(currencyCode)
            budgetInteractor.updateBudgetWithCurrency(currencyCode)
        }
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
