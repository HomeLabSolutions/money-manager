package com.d9tilov.moneymanager.currency.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.data.ResultOf
import com.d9tilov.moneymanager.base.ui.navigator.CurrencyNavigator
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyInteractor: CurrencyInteractor,
    private val userInteractor: UserInteractor,
    private val budgetInteractor: BudgetInteractor,
) : BaseViewModel<CurrencyNavigator>() {

    private val currencies: MutableStateFlow<ResultOf<List<DomainCurrency>>> =
        MutableStateFlow(ResultOf.Loading())

    init {
        loadCurrencies()
    }

    fun changeCurrency(currency: DomainCurrency) = viewModelScope.launch(Dispatchers.IO) {
        awaitAll(
            async { userInteractor.updateCurrency(currency.code) },
            async { budgetInteractor.updateBudgetWithCurrency(currency.code) }
        )
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
            delay(300)
            currencies.value = ResultOf.Loading()
            currencyInteractor.getCurrencies()
                .catch { currencies.value = ResultOf.Failure(it) }
                .flowOn(Dispatchers.IO)
                .collect { currencies.value = ResultOf.Success(it) }
        }

    fun currencies(): StateFlow<ResultOf<List<DomainCurrency>>> = currencies
}
