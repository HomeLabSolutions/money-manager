package com.d9tilov.moneymanager.billing.domain

import androidx.lifecycle.LifecycleObserver
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.d9tilov.moneymanager.billing.domain.entity.BillingSkuDetails
import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency
import kotlinx.coroutines.flow.Flow

interface BillingInteractor {

    fun getObserver(): LifecycleObserver
    fun isPremium(): Flow<Boolean>
    fun getSkuDetails(): Flow<List<BillingSkuDetails>>
    fun getActiveSku(): Flow<BillingSkuDetails?>
    fun getMinPrice(): Flow<DomainCurrency>
    fun buySku(
        sku: String,
        result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult
    )

    fun canPurchase(): Flow<Boolean>
}
