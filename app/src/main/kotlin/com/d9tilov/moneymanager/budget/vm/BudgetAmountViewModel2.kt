package com.d9tilov.moneymanager.budget.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.BudgetAmountNavigator
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.constants.DataConstants.DEFAULT_CURRENCY_SYMBOL
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.CurrencyUtils.getSymbolByCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class BudgetUiState(
    val budgetSum: String = "",
    val currencySymbol: String = DEFAULT_CURRENCY_SYMBOL
)

@HiltViewModel
class BudgetAmountViewModel2 @Inject constructor(
    private val budgetInteractor: BudgetInteractor
) : BaseViewModel<BudgetAmountNavigator>() {

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            budgetInteractor.get()
                .collect { budget ->
                    _uiState.update {
                        it.copy(
                            budgetSum = budget.sum.toString(),
                            currencySymbol = budget.currencyCode.getSymbolByCode()
                        )
                    }
                }
        }
    }

    fun changeBudgetAmount(amount: String) {
        _uiState.update { it.copy(budgetSum = amount) }
    }

    fun saveBudgetAmount() = viewModelScope.launch(Dispatchers.IO) {
        val budget = budgetInteractor.get().firstOrNull()
        budget?.let {
            budgetInteractor.update(
                budget.copy(
                    sum = _uiState.value.budgetSum.toBigDecimalOrNull() ?: BigDecimal.ZERO
                )
            )
        }
    }
}
