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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CurrencyUiState {
    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>

    data class NoCurrencies(
        override val isLoading: Boolean = true,
        override val errorMessages: List<ErrorMessage> = emptyList(),
    ) : CurrencyUiState

    data class HasCurrencies(
        val currencyList: List<DomainCurrency> = emptyList(),
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage> = emptyList(),
    ) : CurrencyUiState
}

@HiltViewModel
class CurrencyViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        currencyInteractor: CurrencyInteractor,
        private val currencyUpdateObserver: CurrencyUpdateObserver,
    ) : ViewModel() {
        private val currencyArgs: CurrencyArgs.CurrencyScreenArgs =
            CurrencyArgs.CurrencyScreenArgs(savedStateHandle)
        private val selectedCurrency: String? = currencyArgs.currencyCode

        private val _uiState: MutableStateFlow<CurrencyUiState> =
            MutableStateFlow(CurrencyUiState.NoCurrencies())
        val uiState = _uiState

        init {
            viewModelScope.launch {
                currencyInteractor
                    .getCurrencies()
                    .map { CurrencyUiState.HasCurrencies(it, false) }
                    .collect { newState: CurrencyUiState.HasCurrencies ->
                        _uiState.update { state ->
                            if (selectedCurrency != null) {
                                val list =
                                    newState.currencyList.map { item ->
                                        item.copy(isBase = item.code == selectedCurrency)
                                    }
                                newState.copy(currencyList = list)
                            } else {
                                newState
                            }
                        }
                    }
            }
        }

        fun changeCurrency(code: String) =
            viewModelScope.launch {
                if (selectedCurrency == null) currencyUpdateObserver.updateMainCurrency(code)
            }
    }
