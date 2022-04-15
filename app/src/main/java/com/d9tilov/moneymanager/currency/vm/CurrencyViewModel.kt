package com.d9tilov.moneymanager.currency.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.data.ResultOf
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.base.ui.navigator.CurrencyNavigator
import com.d9tilov.moneymanager.budget.data.entity.BudgetData
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val currencyInteractor: CurrencyInteractor,
    private val userInteractor: UserInteractor,
    private val budgetInteractor: BudgetInteractor,
    private val categoryInteractor: CategoryInteractor,
    private val preferencesStore: PreferencesStore
) : BaseViewModel<CurrencyNavigator>() {

    private val currencies: MutableStateFlow<ResultOf<List<DomainCurrency>>> =
        MutableStateFlow(ResultOf.Loading())
    private val auth = FirebaseAuth.getInstance()

    init {
        loadCurrencies()
        createUserAndDefaultCategoriesIfNeeded()
    }

    private fun createUserAndDefaultCategoriesIfNeeded() = viewModelScope.launch(Dispatchers.IO) {
        userInteractor.getCurrentUser()
            .catch {
                userInteractor.createUser(auth.currentUser)
                categoryInteractor.createDefaultCategories()
            }
            .collect()
    }

    fun changeCurrency(currency: DomainCurrency) = viewModelScope.launch(Dispatchers.IO) {
        currencyInteractor.updateCurrentCurrency(currency)
        userInteractor.updateCurrency(currency.code)
        budgetInteractor.updateBudgetWithCurrency(currency.code)
        val newCurrencyList = mutableListOf<DomainCurrency>()
        if (currencies.value is ResultOf.Success) {
            for (item in (currencies.value as ResultOf.Success).data) {
                newCurrencyList.add(item.copy(isBase = item.code == currency.code))
            }
            currencies.value = ResultOf.Success(newCurrencyList)
        }
    }

    fun skip() {
        viewModelScope.launch(Dispatchers.IO) {
            budgetInteractor.create(BudgetData.EMPTY.copy(sum = BigDecimal.ZERO))
            preferencesStore.updatePrefill(true)
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
