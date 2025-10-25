package com.d9tilov.moneymanager.prepopulate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.budget.ui.BudgetUiState
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.core.utils.CurrencyUtils.getSymbolByCode
import com.d9tilov.android.core.utils.reduceScaleStr
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.observer.contract.CurrencyUpdateObserver
import com.d9tilov.android.currency.ui.CurrencyUiState
import com.d9tilov.android.user.domain.contract.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Named

data class PrepopulateUiState(
    val currencyUiState: CurrencyUiState = CurrencyUiState.NoCurrencies(),
    val budgetUiState: BudgetUiState = BudgetUiState(),
)

@HiltViewModel
class PrepopulateViewModel
    @Inject
    constructor(
        @Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
        private val currencyInteractor: CurrencyInteractor,
        private val currencyUpdateObserver: CurrencyUpdateObserver,
        private val budgetInteractor: BudgetInteractor,
        private val userInteractor: UserInteractor,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(PrepopulateUiState())
        val uiState: StateFlow<PrepopulateUiState> = _uiState.asStateFlow()

        init {
            viewModelScope.launch(ioDispatcher) {
                budgetInteractor.create()
                combine(
                    currencyInteractor.getCurrencies(),
                    budgetInteractor.get(),
                ) { currencyList, budget ->
                    PrepopulateUiState(
                        CurrencyUiState.HasCurrencies(currencyList, false),
                        BudgetUiState(budget.sum.reduceScaleStr(), budget.currencyCode.getSymbolByCode()),
                    )
                }.collect { state -> _uiState.update { state } }
            }
        }

        fun changeCurrency(currencyCode: String) =
            viewModelScope.launch(ioDispatcher) {
                currencyUpdateObserver.updateMainCurrency(currencyCode)
            }

        fun changeBudgetAmount(amount: String) {
            _uiState.update { it.copy(budgetUiState = it.budgetUiState.copy(budgetSum = amount)) }
        }

        fun changeAmountToSave(amount: String) {
            _uiState.update { it.copy(budgetUiState = it.budgetUiState.copy(amountToSave = amount)) }
        }

        fun saveBudgetAmountAndComplete() =
            viewModelScope.launch(ioDispatcher) {
                launch {
                    val budget = budgetInteractor.get().firstOrNull()
                    if (budget != null) {
                        budgetInteractor.update(
                            budget.copy(
                                sum =
                                    _uiState.value.budgetUiState.budgetSum
                                        .toBigDecimalOrNull()
                                        ?: BigDecimal.ZERO,
                            ),
                        )
                    }
                }
                launch { userInteractor.prepopulateCompleted() }
            }
    }
