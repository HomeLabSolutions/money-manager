package com.d9tilov.android.incomeexpense.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.common_android.ui.base.BaseViewModel
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.incomeexpense.navigation.IncomeExpenseNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class IncomeExpenseViewModel @Inject constructor(
    private val currencyInteractor: CurrencyInteractor,
    billingInteractor: BillingInteractor
) : BaseViewModel<IncomeExpenseNavigator>() {

    private var currencyCodeStr: MutableStateFlow<String> = MutableStateFlow(DEFAULT_CURRENCY_CODE)
    private val updateCurrencyExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.d("Unable to update currency: $exception")
    }
    private var defaultCurrencyCode: String = DEFAULT_CURRENCY_CODE

    init {
        viewModelScope.launch(Dispatchers.IO + updateCurrencyExceptionHandler) {
            currencyInteractor.getMainCurrencyFlow()
                .collect { currency ->
                    currencyCodeStr.value = currency.code
                    defaultCurrencyCode = currencyCodeStr.value
                }
        }
    }

    val isPremium = billingInteractor.isPremium()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun setCurrencyCode(currencyCode: String) {
        currencyCodeStr.value = currencyCode
    }

    fun setDefaultCurrencyCode() {
        currencyCodeStr.value = defaultCurrencyCode
    }

    fun getCurrencyCodeAsync(): Flow<String> = currencyCodeStr
    fun getCurrencyCode(): String = currencyCodeStr.value
}