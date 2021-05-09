package com.d9tilov.moneymanager.currency.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.CurrencyNavigator
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.getFirstDayOfMonth
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.user.domain.UserInteractor
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.Date

class CurrencyViewModel @ViewModelInject constructor(
    private val currencyInteractor: CurrencyInteractor,
    private val userInteractor: UserInteractor,
    private val budgetInteractor: BudgetInteractor
) :
    BaseViewModel<CurrencyNavigator>() {

    private val currencies = MutableLiveData<List<DomainCurrency>>()

    init {
        loadCurrencies()
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            currencies.value = currencyInteractor.getCurrencies()
        }
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
        currencies.value = newCurrencyList
    }

    fun skip() = viewModelScope.launch {
        val count = budgetInteractor.getCount()
        if (count == 0) {
            budgetInteractor.create(
                BudgetData(
                    sum = BigDecimal.ZERO,
                    fiscalDay = Date().getFirstDayOfMonth()
                )
            )
            userInteractor.getCurrentUser().map {
                userInteractor.updateUser(it.copy(showPrepopulate = false))
            }
            navigator?.skip()
        }
    }

    fun getCurrencies(): LiveData<List<DomainCurrency>> = currencies
}
