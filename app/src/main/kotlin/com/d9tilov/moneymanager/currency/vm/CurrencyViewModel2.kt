package com.d9tilov.moneymanager.currency.vm

import androidx.lifecycle.ViewModel
import com.d9tilov.moneymanager.core.util.ErrorMessage
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
class CurrencyViewModel2 @Inject constructor(
    private val currencyInteractor: CurrencyInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyUiState.NoCurrencies())

    val uiState = _uiState.asStateFlow()
}
