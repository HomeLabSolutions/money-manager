package com.d9tilov.moneymanager.billing.domain

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.d9tilov.moneymanager.billing.domain.entity.BillingSkuDetails
import kotlinx.coroutines.flow.Flow

interface BillingRepo {

    val currentPurchases: Flow<List<Purchase>>
    val premiumProductDetails: Flow<ProductDetails?>
    val isNewPurchaseAcknowledged: Flow<Boolean>
    val hasRenewablePremium: Flow<Boolean>
    val billingConnectionReady: Flow<Boolean>
    fun startBillingConnection()
    fun terminateBillingConnection()
    fun getSkuDetails(): Flow<List<BillingSkuDetails>>
    fun buySku(
        tag: String,
        productDetails: ProductDetails?,
        currentPurchases: List<Purchase>,
        result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult
    )
}
