package com.d9tilov.moneymanager.prepopulate.ui

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.base.data.ResultOf
import com.d9tilov.moneymanager.base.ui.navigator.GoalsNavigator
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.budget.domain.entity.BudgetData
import com.d9tilov.moneymanager.budget.vm.BudgetUiState
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.ErrorMessage
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.currency.vm.CurrencyUiState
import com.d9tilov.moneymanager.currency.vm.CurrencyViewModel
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PrepopulateUiState(
    val currencyUiState: CurrencyUiState,
    val budgetUiState: BudgetUiState
)

@HiltViewModel
class PrepopulateViewModel @Inject constructor(
    private val currencyInteractor: CurrencyInteractor,
    private val budgetInteractor: BudgetInteractor
) : BaseViewModel<GoalsNavigator>() {

    private val _uiState = MutableStateFlow(
        PrepopulateUiState(
            currencyUiState = CurrencyUiState.NoCurrencies(isLoading = true),
            budgetUiState = BudgetUiState(BudgetData.EMPTY)
        )
    )
    val uiState: StateFlow<PrepopulateUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            currencyInteractor.getCurrencies()
                .catch { error: Throwable ->
                    _uiState.update {
                        it.copy(
                            currencyUiState = CurrencyUiState.NoCurrencies(
                                isLoading = false, errorMessages = listOf(
                                    ErrorMessage(0L, R.string.currency_error, error)
                                )
                            )
                        )
                    }
                }
                .flowOn(Dispatchers.IO)
                .collect { list ->
                    _uiState.update {
                        it.copy(
                            currencyUiState = CurrencyUiState.HasCurrencies(
                                currencyList = list,
                                isLoading = false
                            )
                        )
                    }
                }
        }
    }
}
