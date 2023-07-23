package com.d9tilov.android.goals.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GoalsUiState(
    val canBeSpend: String = "",
    val saveEveryPeriod: String = ""
)

@HiltViewModel
class GoalsViewModel @Inject constructor(private val budgetInteractor: BudgetInteractor) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            budgetInteractor.get()
                .collect { budget ->
                    _uiState.update {
                        it.copy(
//                            budgetSum = budget.sum.toString(),
//                            currencySymbol = budget.currencyCode.getSymbolByCode()
                        )
                    }
                }
        }
    }

    fun changeBudgetAmount(amount: String) {
//        _uiState.update { it.copy(budgetSum = amount) }
    }

    fun saveBudgetAmount() = viewModelScope.launch(Dispatchers.IO) {
        val budget = budgetInteractor.get().firstOrNull()
        budget?.let {
            budgetInteractor.update(
                budget.copy(
//                    sum = _uiState.value.budgetSum.toBigDecimalOrNull() ?: BigDecimal.ZERO
                )
            )
        }
    }
}
