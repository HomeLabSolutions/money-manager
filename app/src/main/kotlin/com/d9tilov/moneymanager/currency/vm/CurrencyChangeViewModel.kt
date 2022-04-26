package com.d9tilov.moneymanager.currency.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.CurrencyChangeNavigator
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrencyChangeViewModel @Inject constructor(
    private val currencyInteractor: CurrencyInteractor,
    private val userInteractor: UserInteractor,
    private val budgetInteractor: BudgetInteractor
) : BaseViewModel<CurrencyChangeNavigator>() {

    fun changeCurrency(currency: DomainCurrency) = viewModelScope.launch(Dispatchers.IO) {
        userInteractor.updateCurrency(currency.code)
        budgetInteractor.updateBudgetWithCurrency(currency.code)
        withContext(Dispatchers.Main) { navigator?.change() }
    }
}
