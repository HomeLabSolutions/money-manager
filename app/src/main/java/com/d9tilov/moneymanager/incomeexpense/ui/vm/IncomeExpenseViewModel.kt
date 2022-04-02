package com.d9tilov.moneymanager.incomeexpense.ui.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.base.ui.navigator.IncomeExpenseNavigator
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class IncomeExpenseViewModel @Inject constructor(
    private val transactionInteractor: TransactionInteractor,
    private val currencyInteractor: CurrencyInteractor
) :
    BaseViewModel<IncomeExpenseNavigator>() {

    private var currencyCodeStr: MutableLiveData<String> = MutableLiveData(DEFAULT_CURRENCY_CODE)
    private val updateCurrencyExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.d("Unable to update currency: $exception")
    }

    private var defaultCurrencyCode: String = DEFAULT_CURRENCY_CODE

    init {
        viewModelScope.launch(Dispatchers.IO + updateCurrencyExceptionHandler) {
            currencyInteractor.updateCurrencyRates()
        }
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                currencyCodeStr.value = currencyInteractor.getCurrentCurrency().code
                defaultCurrencyCode = currencyCodeStr.value ?: DEFAULT_CURRENCY_CODE
            }
            transactionInteractor.executeRegularIfNeeded(TransactionType.INCOME)
            transactionInteractor.executeRegularIfNeeded(TransactionType.EXPENSE)
        }
    }

    fun setCurrencyCode(currencyCode: String) {
        currencyCodeStr.value = currencyCode
    }

    fun setDefaultCurrencyCode() {
        currencyCodeStr.value = defaultCurrencyCode
    }

    fun getCurrencyCodeAsync(): LiveData<String> = currencyCodeStr
    fun getCurrencyCode(): String = currencyCodeStr.value ?: DEFAULT_CURRENCY_CODE
}
