package com.d9tilov.moneymanager.settings

import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.d9tilov.moneymanager.base.ui.navigator.SettingsBillingNavigator
import com.d9tilov.moneymanager.billing.domain.BillingInteractor
import com.d9tilov.moneymanager.core.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsBillingIntroViewModel @Inject constructor(private val billingInteractor: BillingInteractor) :
    BaseViewModel<SettingsBillingNavigator>() {

    private var currentPurchases: List<Purchase> = listOf()
    private var productDetails: ProductDetails? = null

    init {
        viewModelScope.launch {
            launch {
                billingInteractor.currentPurchases
                    .flowOn(Dispatchers.IO)
                    .collect { currentPurchases = it }
            }
            launch {
                billingInteractor.productDetails
                    .flowOn(Dispatchers.IO)
                    .collect { productDetails = it }
            }
        }
    }

    val skuDetails = billingInteractor.getSkuDetails()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val purchaseCompleted = billingInteractor.isNewPurchaseAcknowledged
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun buySubscription(
        tag: String,
        result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult
    ) {
        billingInteractor.buySku(tag, productDetails, currentPurchases, result)
    }
}
