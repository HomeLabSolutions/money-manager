package com.d9tilov.moneymanager.currency.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.data.Result
import com.d9tilov.moneymanager.base.ui.navigator.CurrencyNavigator
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyInteractor: CurrencyInteractor,
    private val userInteractor: UserInteractor,
    private val budgetInteractor: BudgetInteractor
) : BaseViewModel<CurrencyNavigator>() {

    private val currencies = MutableLiveData<Result<List<DomainCurrency>>>()

    init {
        getCurrencies()
    }

    fun changeCurrency(currency: DomainCurrency) = viewModelScope.launch(Dispatchers.IO) {
        currencyInteractor.updateCurrentCurrency(currency)
        userInteractor.updateCurrency(currency.code)
        budgetInteractor.updateBudgetWithCurrency(currency.code)
        val newCurrencyList = mutableListOf<DomainCurrency>()
        for (item in currencies.value?.data ?: emptyList()) {
            newCurrencyList.add(item.copy(isBase = item.code == currency.code))
        }
        currencies.postValue(Result.success(newCurrencyList))
    }

    fun skip() {
        viewModelScope.launch(Dispatchers.IO) {
            budgetInteractor.create(
                BudgetData(sum = BigDecimal.ZERO)
            )
            val user = userInteractor.getCurrentUser().first()
            userInteractor.updateUser(user.copy(showPrepopulate = false))
        }
        navigator?.skip()
    }

    fun getCurrencies() =
        viewModelScope.launch {
            delay(300)
            currencies.value = Result.loading()
            currencyInteractor.getCurrencies()
                .catch { currencies.postValue(Result.error(it)) }
                .flowOn(Dispatchers.IO)
                .collect { currencies.value = Result.success(it) }
        }

    fun currencies(): LiveData<Result<List<DomainCurrency>>> = currencies
}
