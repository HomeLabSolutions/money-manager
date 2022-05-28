package com.d9tilov.moneymanager.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.moneymanager.billing.domain.BillingInteractor
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userInteractor: UserInteractor,
    private val currencyInteractor: CurrencyInteractor,
    private val transactionInteractor: TransactionInteractor,
    billingInteractor: BillingInteractor
) : ViewModel() {

    private val updateCurrencyExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.d("Unable to update currency: $exception")
    }

    val currency =
        userInteractor.getCurrentCurrency().shareIn(viewModelScope, SharingStarted.Eagerly)
    val isPremium = billingInteractor.isPremium()
        .flowOn(Dispatchers.IO)
        .shareIn(viewModelScope, SharingStarted.Eagerly)

    init {
        viewModelScope.launch(Dispatchers.IO + updateCurrencyExceptionHandler) {
            isPremium
                .collect { isPremium ->
                    if (isPremium) {
                        launch {
                            transactionInteractor.executeRegularIfNeeded(TransactionType.INCOME)
                            transactionInteractor.executeRegularIfNeeded(TransactionType.EXPENSE)
                        }
                    }
                }
            launch { currencyInteractor.updateCurrencyRates() }
        }
    }
}
