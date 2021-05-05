package com.d9tilov.moneymanager.currency.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.d9tilov.moneymanager.base.ui.navigator.CurrencyNavigator
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.core.util.addTo
import com.d9tilov.moneymanager.core.util.getFirstDayOfMonth
import com.d9tilov.moneymanager.core.util.ioScheduler
import com.d9tilov.moneymanager.core.util.uiScheduler
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.user.domain.UserInteractor
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
        currencyInteractor.getCurrencies()
            .toObservable()
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .doOnError { navigator?.showError() }
            .retryWhen { it.flatMap { retryManager.observeRetries() } }
            .subscribe { list -> currencies.value = list }
            .addTo(compositeDisposable)
    }

    fun updateBaseCurrency(currency: DomainCurrency) {
        currencyInteractor.updateBaseCurrency(currency)
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe {
                val newCurrencyList = mutableListOf<DomainCurrency>()
                for (item in currencies.value ?: emptyList()) {
                    newCurrencyList.add(item.copy(isBase = item.code == currency.code))
                }
                currencies.value = newCurrencyList
            }
            .addTo(compositeDisposable)
    }

    fun skip() {
        budgetInteractor.getCount()
            .filter { it == 0 }
            .flatMapCompletable {
                budgetInteractor.create(
                    BudgetData(
                        sum = BigDecimal.ZERO,
                        fiscalDay = Date().getFirstDayOfMonth()
                    )
                )
            }
            .andThen(
                userInteractor.getCurrentUser()
                    .firstOrError()
                    .flatMapCompletable { userInteractor.updateUser(it.copy(showPrepopulate = false)) }
            )
            .subscribeOn(ioScheduler)
            .observeOn(uiScheduler)
            .subscribe { navigator?.skip() }
            .addTo(compositeDisposable)
    }

    fun getCurrencies(): LiveData<List<DomainCurrency>> = currencies
}
