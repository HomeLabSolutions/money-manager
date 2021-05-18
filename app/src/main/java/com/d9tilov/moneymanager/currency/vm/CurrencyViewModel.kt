package com.d9tilov.moneymanager.currency.vm

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.CurrencyNavigator
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.getFirstDayOfMonth
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyInteractor: CurrencyInteractor,
    private val userInteractor: UserInteractor,
    private val budgetInteractor: BudgetInteractor
) : BaseViewModel<CurrencyNavigator>() {

    val currencies = currencyInteractor.getCurrencies().distinctUntilChanged().asLiveData()

    private fun loadCurrencies() {
        //
        // TODO: make retry
        // currencyInteractor.getCurrencies()
        //     .toObservable()
        //     .subscribeOn(ioScheduler)
        //     .observeOn(uiScheduler)
        //     .doOnError { navigator?.showError() }
        //     .retryWhen { it.flatMap { retryManager.observeRetries() } }
        //     .subscribe { list -> currencies.value = list }
        //     .addTo(compositeDisposable)
    }

    fun updateBaseCurrency(currency: DomainCurrency) = viewModelScope.launch {
        currencyInteractor.updateBaseCurrency(currency)
        val newCurrencyList = mutableListOf<DomainCurrency>()
        for (item in currencies.value ?: emptyList()) {
            newCurrencyList.add(item.copy(isBase = item.code == currency.code))
        }
    }

    fun skip() = viewModelScope.launch {
        budgetInteractor.create(
            BudgetData(
                sum = BigDecimal.ZERO,
                fiscalDay = getFirstDayOfMonth()
            )
        )
        val user = userInteractor.getCurrentUser().first()
        userInteractor.updateUser(user.copy(showPrepopulate = false))
        navigator?.skip()
    }
}
