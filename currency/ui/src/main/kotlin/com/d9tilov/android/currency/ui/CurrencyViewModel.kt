package com.d9tilov.android.currency.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.analytics.model.AnalyticsEvent
import com.d9tilov.android.analytics.model.AnalyticsParams
import com.d9tilov.android.core.constants.DiConstants.DISPATCHER_IO
import com.d9tilov.android.core.model.ErrorMessage
import com.d9tilov.android.core.utils.CurrencyUtils
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.currency.domain.model.DomainCurrency
import com.d9tilov.android.currency.observer.contract.CurrencyUpdateObserver
import com.d9tilov.android.currency.ui.navigation.CurrencyArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

sealed interface CurrencyUiState {
    val isLoading: Boolean
    val errorMessages: List<ErrorMessage>

    data class NoCurrencies(
        override val isLoading: Boolean = true,
        override val errorMessages: List<ErrorMessage> = emptyList(),
    ) : CurrencyUiState

    data class HasCurrencies(
        val currencyList: List<DomainCurrency> = emptyList(),
        val filteredCurrencyList: List<DomainCurrency> = emptyList(),
        val searchQuery: String = "",
        val isSearchActive: Boolean = false,
        override val isLoading: Boolean,
        override val errorMessages: List<ErrorMessage> = emptyList(),
    ) : CurrencyUiState
}

@HiltViewModel
class CurrencyViewModel
    @Inject
    constructor(
        @Named(DISPATCHER_IO) private val ioDispatcher: CoroutineDispatcher,
        savedStateHandle: SavedStateHandle,
        currencyInteractor: CurrencyInteractor,
        analyticsSender: AnalyticsSender,
        private val currencyUpdateObserver: CurrencyUpdateObserver,
    ) : ViewModel() {
        private val currencyArgs: CurrencyArgs.CurrencyScreenArgs =
            CurrencyArgs.CurrencyScreenArgs(savedStateHandle)
        private val selectedCurrency: String? = currencyArgs.currencyCode

        private val _uiState: MutableStateFlow<CurrencyUiState> =
            MutableStateFlow(CurrencyUiState.NoCurrencies())
        val uiState = _uiState

        init {
            analyticsSender.send(
                AnalyticsEvent.Internal.Screen,
                mapOf(
                    AnalyticsParams.Screen.Name to "currency_list",
                    AnalyticsParams.Currency to selectedCurrency,
                ),
            )
            viewModelScope.launch(ioDispatcher) {
                currencyInteractor
                    .getCurrencies()
                    .map {
                        CurrencyUiState.HasCurrencies(
                            currencyList = it,
                            filteredCurrencyList = it,
                            isLoading = false,
                        )
                    }.collect { newState: CurrencyUiState.HasCurrencies ->
                        _uiState.update { state ->
                            val updatedList =
                                if (selectedCurrency != null) {
                                    newState.currencyList.map { item ->
                                        item.copy(isBase = item.code == selectedCurrency)
                                    }
                                } else {
                                    newState.currencyList
                                }
                            newState.copy(
                                currencyList = updatedList,
                                filteredCurrencyList = updatedList,
                            )
                        }
                    }
            }
        }

        fun changeCurrency(code: String) =
            viewModelScope.launch {
                if (selectedCurrency == null) currencyUpdateObserver.updateMainCurrency(code)
            }

        fun updateSearchQuery(query: String) {
            _uiState.update { state ->
                if (state is CurrencyUiState.HasCurrencies) {
                    val filtered = filterCurrencies(state.currencyList, query)
                    state.copy(searchQuery = query, filteredCurrencyList = filtered)
                } else {
                    state
                }
            }
        }

        fun setSearchActive(active: Boolean) {
            _uiState.update { state ->
                if (state is CurrencyUiState.HasCurrencies) {
                    val query = if (!active) "" else state.searchQuery
                    val filtered = filterCurrencies(state.currencyList, query)
                    state.copy(
                        isSearchActive = active,
                        searchQuery = query,
                        filteredCurrencyList = filtered,
                    )
                } else {
                    state
                }
            }
        }

        private fun filterCurrencies(
            currencies: List<DomainCurrency>,
            query: String,
        ): List<DomainCurrency> =
            currencies.filter { currency ->
                currency.code.contains(query, ignoreCase = true) ||
                    CurrencyUtils.getCurrencyFullName(currency.code).contains(query, ignoreCase = true)
            }
    }
