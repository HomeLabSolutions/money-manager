package com.d9tilov.moneymanager.currency.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.core.util.ErrorMessage
import com.d9tilov.android.interactor.CurrencyInteractor
import com.d9tilov.android.interactor.UpdateCurrencyInteractor
import com.d9tilov.android.interactor.model.DomainCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
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
        val currencyList: List<com.d9tilov.android.interactor.model.DomainCurrency> = emptyList(),
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage> = emptyList()
    ) : CurrencyUiState
}

@HiltViewModel
class CurrencyViewModel2 @Inject constructor(
    currencyInteractor: com.d9tilov.android.interactor.CurrencyInteractor,
    private val updateCurrencyInteractor: com.d9tilov.android.interactor.UpdateCurrencyInteractor
) : ViewModel() {

    val uiState = currencyInteractor.getCurrencies()
        .map { CurrencyUiState.HasCurrencies(it, false) }
        .flowOn(Dispatchers.IO)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = CurrencyUiState.NoCurrencies()
        )

    fun changeCurrency(code: String) = viewModelScope.launch(Dispatchers.IO) {
        updateCurrencyInteractor.updateCurrency(code)
    }
}
