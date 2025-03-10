package com.d9tilov.android.billing.data.contract

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import kotlinx.coroutines.flow.Flow

interface BillingSource {
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
        result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult,
    )
}
