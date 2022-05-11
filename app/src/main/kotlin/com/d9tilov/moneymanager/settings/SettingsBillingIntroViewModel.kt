package com.d9tilov.moneymanager.settings

import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.d9tilov.moneymanager.base.ui.navigator.SettingsBillingNavigator
import com.d9tilov.moneymanager.billing.domain.BillingInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class SettingsBillingIntroViewModel @Inject constructor(private val billingInteractor: BillingInteractor) :
    BaseViewModel<SettingsBillingNavigator>() {

    val lifecycleObserver = billingInteractor.getObserver()

    val skuDetails = billingInteractor.getSkuDetails()
        .flowOn(Dispatchers.IO)
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed())
    val purchaseCompleted = billingInteractor.purchaseCompleted()
        .flowOn(Dispatchers.IO)
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    fun buySubscription(
        sku: String,
        result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult
    ) {
        billingInteractor.buySku(sku, result)
    }
}
