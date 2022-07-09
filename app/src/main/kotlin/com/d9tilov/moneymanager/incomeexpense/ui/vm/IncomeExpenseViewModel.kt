package com.d9tilov.moneymanager.incomeexpense.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.IncomeExpenseNavigator
import com.d9tilov.moneymanager.billing.domain.BillingInteractor
import com.d9tilov.moneymanager.core.constants.DataConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class IncomeExpenseViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    billingInteractor: BillingInteractor
) : BaseViewModel<IncomeExpenseNavigator>() {

    private var currencyCodeStr: MutableStateFlow<String> = MutableStateFlow(DEFAULT_CURRENCY_CODE)
    private val updateCurrencyExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.d("Unable to update currency: $exception")
    }
    private var defaultCurrencyCode: String = DEFAULT_CURRENCY_CODE

    init {
        viewModelScope.launch(Dispatchers.IO + updateCurrencyExceptionHandler) {
            userInteractor.getCurrentCurrency()
                .collect { currency ->
                    currencyCodeStr.value = currency.code
                    defaultCurrencyCode = currencyCodeStr.value
                }
        }
    }

    fun setCurrencyCode(currencyCode: String) {
        currencyCodeStr.value = currencyCode
    }

    fun setDefaultCurrencyCode() {
        currencyCodeStr.value = defaultCurrencyCode
    }

    fun getCurrencyCodeAsync(): Flow<String> = currencyCodeStr
    fun getCurrencyCode(): String = currencyCodeStr.value
}
