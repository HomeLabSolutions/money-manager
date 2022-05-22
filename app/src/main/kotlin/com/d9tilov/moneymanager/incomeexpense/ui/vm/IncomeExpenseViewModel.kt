package com.d9tilov.moneymanager.incomeexpense.ui.vm

import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.backup.PeriodicBackupWorker
import com.d9tilov.moneymanager.base.ui.navigator.IncomeExpenseNavigator
import com.d9tilov.moneymanager.billing.domain.BillingInteractor
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_CURRENCY_CODE
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class IncomeExpenseViewModel @Inject constructor(
    private val userInteractor: UserInteractor,
    private val currencyInteractor: CurrencyInteractor,
    private val transactionInteractor: TransactionInteractor,
    private val billingInteractor: BillingInteractor
) : BaseViewModel<IncomeExpenseNavigator>() {

    private var currencyCodeStr: MutableStateFlow<String> = MutableStateFlow(DEFAULT_CURRENCY_CODE)
    private val updateCurrencyExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.d("Unable to update currency: $exception")
    }
    private var defaultCurrencyCode: String = DEFAULT_CURRENCY_CODE

    init {
        viewModelScope.launch(Dispatchers.IO + updateCurrencyExceptionHandler) {
            currencyInteractor.updateCurrencyRates()
        }
        viewModelScope.launch(Dispatchers.IO) {
            val currencyCode = userInteractor.getCurrentCurrency()
            currencyCodeStr.value = currencyCode
            defaultCurrencyCode = currencyCodeStr.value
            billingInteractor.isPremium()
                .flowOn(Dispatchers.IO)
                .collect { isPremium ->
                    if (isPremium) {
                        awaitAll(
                            async { transactionInteractor.executeRegularIfNeeded(TransactionType.INCOME) },
                            async { transactionInteractor.executeRegularIfNeeded(TransactionType.EXPENSE) },
                        )
                    }
                }
        }
    }

    val isPremium = billingInteractor.isPremium()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun setCurrencyCode(currencyCode: String) {
        currencyCodeStr.value = currencyCode
    }

    fun setDefaultCurrencyCode() {
        currencyCodeStr.value = defaultCurrencyCode
    }

    fun getCurrencyCodeAsync(): Flow<String> = currencyCodeStr
    fun getCurrencyCode(): String = currencyCodeStr.value
}
