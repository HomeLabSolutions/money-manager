package com.d9tilov.android.budget.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.analytics.model.AnalyticsEvent
import com.d9tilov.android.analytics.model.AnalyticsParams
import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_SYMBOL
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.reduceScaleStr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class BudgetUiState(
    val budgetSum: String = "",
    val amountToSave: String = "",
    val currencySymbol: String = DEFAULT_CURRENCY_SYMBOL,
)

@HiltViewModel
class BudgetAmountViewModel
    @Inject
    constructor(
        analyticsSender: AnalyticsSender,
        private val budgetInteractor: BudgetInteractor,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(BudgetUiState())
        val uiState = _uiState.asStateFlow()

        init {
            analyticsSender.send(
                AnalyticsEvent.Internal.Screen,
                mapOf(AnalyticsParams.Screen.Name to "budget"),
            )
            viewModelScope.launch {
                budgetInteractor
                    .get()
                    .collect { budget ->
                        _uiState.update {
                            it.copy(
                                budgetSum = budget.sum.reduceScaleStr(),
                                amountToSave = budget.saveSum.reduceScaleStr(),
                                currencySymbol = budget.currencyCode.getSymbolByCode(),
                            )
                        }
                    }
            }
        }

        fun changeBudgetAmount(amount: String) {
            _uiState.update { it.copy(budgetSum = amount) }
        }

        fun changeAmountToSave(amount: String) {
            _uiState.update { it.copy(amountToSave = amount) }
        }

        fun saveBudgetAmount() =
            viewModelScope.launch {
                val budget = budgetInteractor.get().firstOrNull()
                budget?.let {
                    budgetInteractor.update(
                        budget.copy(
                            sum = _uiState.value.budgetSum.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                            saveSum = _uiState.value.amountToSave.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                        ),
                    )
                }
            }
    }
