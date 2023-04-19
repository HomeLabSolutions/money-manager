package com.d9tilov.moneymanager.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.android.core.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val transactionInteractor: TransactionInteractor,
    private val billingInteractor: com.d9tilov.android.billing.domain.contract.BillingInteractor
) : ViewModel() {

    private val updateCurrencyExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.d("Unable to update currency: $exception")
    }

    val isPremium = billingInteractor.isPremium()
        .flowOn(Dispatchers.IO)
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)

    init {
        billingInteractor.startBillingConnection()
        viewModelScope.launch(Dispatchers.IO + updateCurrencyExceptionHandler) {
            billingInteractor.billingConnectionReady.combine(isPremium) { isReady, isPremium ->
                isReady && isPremium
            }.collect { readyForPremium ->
                if (readyForPremium) {
                    launch { transactionInteractor.executeRegularIfNeeded(TransactionType.INCOME) }
                    launch { transactionInteractor.executeRegularIfNeeded(TransactionType.EXPENSE) }
                }
            }
        }
    }

    override fun onCleared() {
        billingInteractor.terminateBillingConnection()
    }
}
