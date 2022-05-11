package com.d9tilov.moneymanager.billing.domain

import androidx.lifecycle.LifecycleObserver
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.d9tilov.moneymanager.billing.domain.entity.BillingSkuDetails
import com.d9tilov.moneymanager.currency.data.entity.Currency
import kotlinx.coroutines.flow.Flow

interface BillingRepo {

    fun getObserver(): LifecycleObserver
    fun isPremium(): Flow<Boolean>
    fun getSkuDetails(): Flow<List<BillingSkuDetails>>
    fun getActiveSku(): Flow<BillingSkuDetails?>
    fun getMinPrice(): Flow<Currency>
    fun buySku(
        sku: String,
        result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult
    )

    fun purchaseCompleted(): Flow<Boolean>
    fun canPurchase(): Flow<Boolean>
}
