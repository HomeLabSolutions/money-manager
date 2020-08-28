package com.d9tilov.moneymanager.prepopulate.currency.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.base.ui.navigator.CurrencyNavigator
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.prepopulate.currency.data.entity.Currency
import com.d9tilov.moneymanager.prepopulate.currency.domain.CurrencyInteractor

class CurrencyViewModel @ViewModelInject constructor(private val currencyInteractor: CurrencyInteractor) :
    BaseViewModel<CurrencyNavigator>() {

    val currencies = MutableLiveData<List<Currency>>()

    init {
        loadCurrencies()
    }

    private fun loadCurrencies() {
        currencyInteractor.getCurrencies()
            .toObservable()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .doOnError { navigator?.showError() }
            .retryWhen { it.flatMap { retryManager.observeRetries() } }
            .subscribe { list ->
                currencies.value = list
            }
            .addTo(compositeDisposable)
    }

    fun updateBaseCurrency(currency: Currency) {
        currencyInteractor.updateBaseCurrency(currency)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe {
                val newCurrencyList = mutableListOf<Currency>()
                for (item in currencies.value ?: emptyList()) {
                    newCurrencyList.add(item.copy(isBase = item.code == currency.code))
                }
                currencies.value = newCurrencyList
            }
            .addTo(compositeDisposable)
    }
}
