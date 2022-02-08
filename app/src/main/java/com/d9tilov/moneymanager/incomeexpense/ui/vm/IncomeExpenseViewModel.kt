package com.d9tilov.moneymanager.incomeexpense.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.IncomeExpenseNavigator
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class IncomeExpenseViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val transactionInteractor: TransactionInteractor,
    private val currencyInteractor: CurrencyInteractor
) :
    BaseViewModel<IncomeExpenseNavigator>() {

    private var currencyCodeStr: MutableLiveData<String> = MutableLiveData(DEFAULT_CURRENCY_CODE)
    private val updateCurrencyExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.d("Unable to update currency: $exception")
    }

    init {
        viewModelScope.launch { currencyCodeStr.value = userInteractor.getCurrentCurrency() }
        viewModelScope.launch(Dispatchers.IO + updateCurrencyExceptionHandler) {
            currencyInteractor.updateCurrencyRates()
        }
    }

    fun setCurrencyCode(currencyCode: String) {
        currencyCodeStr.value = currencyCode
    }

    fun setDefaultCurrencyCode() {
        viewModelScope.launch { currencyCodeStr.value = userInteractor.getCurrentCurrency() }
    }

    fun getCurrencyCode(): LiveData<String> = currencyCodeStr
}
