package com.d9tilov.moneymanager.billing.domain

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.d9tilov.moneymanager.billing.domain.entity.BillingSkuDetails
import com.d9tilov.moneymanager.billing.domain.entity.PremiumInfo
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import kotlinx.coroutines.flow.Flow

interface BillingInteractor {

    val currentPurchases: Flow<List<Purchase>>
    val productDetails: Flow<ProductDetails?>
    val isNewPurchaseAcknowledged: Flow<Boolean>
    val billingConnectionReady: Flow<Boolean>
    fun getPremiumInfo(): Flow<PremiumInfo>
    fun startBillingConnection()
    fun terminateBillingConnection()
    fun buySku(
        tag: String,
        productDetails: ProductDetails?,
        currentPurchases: List<Purchase>,
        result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult
    )

    fun isPremium(): Flow<Boolean>
    fun getSkuDetails(): Flow<List<BillingSkuDetails>>
}
