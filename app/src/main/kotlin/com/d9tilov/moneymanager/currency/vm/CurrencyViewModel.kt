package com.d9tilov.moneymanager.currency.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.data.ResultOf
import com.d9tilov.moneymanager.base.ui.navigator.CurrencyNavigator
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.ErrorMessage
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

sealed interface CurrencyUiState {
    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>

    data class NoCurrencies(
        override val isLoading: Boolean = true,
        override val errorMessages: List<ErrorMessage> = emptyList()
    ) : CurrencyUiState

    data class HasCurrencies(
        val currencyList: List<DomainCurrency> = emptyList(),
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage> = emptyList()
    ) : CurrencyUiState
}

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyInteractor: CurrencyInteractor,
    private val userInteractor: UserInteractor,
    private val budgetInteractor: BudgetInteractor
) : BaseViewModel<CurrencyNavigator>() {

    private val viewModelState = MutableStateFlow(CurrencyViewModelState(isLoading = true))

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    private val currencies: MutableStateFlow<ResultOf<List<DomainCurrency>>> =
        MutableStateFlow(ResultOf.Loading())

    init {
        loadCurrencies()
    }

    fun changeCurrency(currency: DomainCurrency) = viewModelScope.launch(Dispatchers.IO) {
        launch { userInteractor.updateCurrency(currency.code) }
        launch { budgetInteractor.updateBudgetWithCurrency(currency.code) }
    }

    fun createBudgetAndSkip() {
        viewModelScope.launch(Dispatchers.IO) {
            awaitAll(
                async { budgetInteractor.create() },
                async { userInteractor.prepopulateCompleted() }
            )
            withContext(Dispatchers.Main) { navigator?.skip() }
        }
    }

    fun loadCurrencies() =
        viewModelScope.launch {
            delay(LOAD_CURRENCIES_DELAY)
            currencies.value = ResultOf.Loading()
            currencyInteractor.getCurrencies()
                .catch { currencies.value = ResultOf.Failure(it) }
                .flowOn(Dispatchers.IO)
                .collect { currencies.value = ResultOf.Success(it) }
        }

    fun currencies(): StateFlow<ResultOf<List<DomainCurrency>>> = currencies

    companion object {
        private const val LOAD_CURRENCIES_DELAY = 300L
    }
}

private data class CurrencyViewModelState(
    val currencyList: List<DomainCurrency> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList()
) {

    fun toUiState(): CurrencyUiState =
        if (currencyList.isEmpty()) CurrencyUiState.NoCurrencies(
            isLoading = isLoading,
            errorMessages = errorMessages
        ) else CurrencyUiState.HasCurrencies(
            currencyList = currencyList,
            isLoading = isLoading,
            errorMessages = errorMessages
        )
}
