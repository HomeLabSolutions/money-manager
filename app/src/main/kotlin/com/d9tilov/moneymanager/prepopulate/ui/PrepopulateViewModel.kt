package com.d9tilov.moneymanager.prepopulate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.budget.ui.BudgetUiState
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.contract.UpdateCurrencyInteractor
import com.d9tilov.android.ui.CurrencyUiState
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
                    BudgetUiState(budget.sum.toString(), budget.currencyCode.getSymbolByCode())
                )
            }.collect { state -> _uiState.update { state } }
        }
    }

    fun changeCurrency(currencyCode: String) = viewModelScope.launch(Dispatchers.IO) {
        updateCurrencyInteractor.updateCurrency(currencyCode)
    }

    fun changeBudgetAmount(amount: String) {
        _uiState.update { it.copy(budgetUiState = it.budgetUiState.copy(budgetSum = amount)) }
    }

    fun saveBudgetAmountAndComplete() = viewModelScope.launch(Dispatchers.IO) {
        launch {
            val budget = budgetInteractor.get().firstOrNull()
            budget?.let {
                budgetInteractor.update(
                    budget.copy(
                        sum = _uiState.value.budgetUiState.budgetSum.toBigDecimalOrNull()
                            ?: BigDecimal.ZERO
                    )
                )
            }
        }
        launch { userInteractor.prepopulateCompleted() }
    }
}
