package com.d9tilov.moneymanager.billing.data.local

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.d9tilov.android.core.model.Source
import kotlinx.coroutines.flow.Flow

interface BillingSource : Source {

    val purchases: Flow<List<Purchase>>
    val productWithProductDetails: Flow<Map<String, ProductDetails>>
    val isNewPurchaseAcknowledged: Flow<Boolean>
    val billingConnectionReady: Flow<Boolean>
    fun startBillingConnection()
    fun terminateBillingConnection()
    fun buySku(
        tag: String,
        productDetails: ProductDetails?,
        currentPurchases: List<Purchase>,
        result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult
    )
}
