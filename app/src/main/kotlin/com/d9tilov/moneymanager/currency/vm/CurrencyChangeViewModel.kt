package com.d9tilov.moneymanager.currency.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.CurrencyChangeNavigator
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CurrencyChangeViewModel @Inject constructor(
    private val budgetInteractor: BudgetInteractor
) : BaseViewModel<CurrencyChangeNavigator>() {

    fun changeCurrency(currencyCode: String) = viewModelScope.launch(Dispatchers.IO) {
        budgetInteractor.updateBudgetWithCurrency(currencyCode)
        withContext(Dispatchers.Main) { navigator?.change() }
    }
}
