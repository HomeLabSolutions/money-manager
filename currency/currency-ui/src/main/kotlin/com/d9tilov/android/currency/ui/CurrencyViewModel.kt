package com.d9tilov.android.currency.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.core.model.ErrorMessage
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.model.DomainCurrency
import com.d9tilov.android.currency.observer.contract.CurrencyUpdateObserver
import com.d9tilov.android.currency.ui.navigation.CurrencyArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
    savedStateHandle: SavedStateHandle,
    currencyInteractor: CurrencyInteractor,
    private val currencyUpdateObserver: CurrencyUpdateObserver
) : ViewModel() {

    private val currencyArgs: CurrencyArgs.CurrencyScreenArgs =
        CurrencyArgs.CurrencyScreenArgs(savedStateHandle)
    private val changeGlobally:Boolean = checkNotNull(currencyArgs.changeGlobally)

    val uiState = currencyInteractor.getCurrencies()
        .map { CurrencyUiState.HasCurrencies(it, false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CurrencyUiState.NoCurrencies()
        )

    fun changeCurrency(code: String) = viewModelScope.launch {
        if (changeGlobally) currencyUpdateObserver.updateMainCurrency(code)
    }
}
