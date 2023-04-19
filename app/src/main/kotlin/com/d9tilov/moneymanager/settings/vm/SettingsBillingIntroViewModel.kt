package com.d9tilov.moneymanager.settings.vm

import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.d9tilov.moneymanager.base.ui.navigator.SettingsBillingNavigator
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.common_android.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsBillingIntroViewModel @Inject constructor(private val billingInteractor: com.d9tilov.android.billing.domain.contract.BillingInteractor) :
    BaseViewModel<SettingsBillingNavigator>() {

    private var currentPurchases: List<Purchase> = listOf()
    private var productDetails: ProductDetails? = null

    private val skuExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.d("Unable to get sku")
    }

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
        .flowOn(Dispatchers.IO + skuExceptionHandler)
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
