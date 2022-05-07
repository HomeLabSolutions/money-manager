package com.d9tilov.moneymanager.billing.data.local

import androidx.lifecycle.DefaultLifecycleObserver
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.d9tilov.moneymanager.billing.domain.entity.PurchaseMetaData
import com.d9tilov.moneymanager.currency.data.entity.Currency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface BillingSource : DefaultLifecycleObserver {

    fun launchBillingFlow(
        sku: String,
        result: (billingClient: BillingClient, paramBuilder: BillingFlowParams) -> BillingResult,
        vararg upgradeSkusVarargs: String
    )

    fun isPurchased(sku: String): Flow<Boolean>
    fun getSkuTitle(sku: String): Flow<String>
    fun getSkuDescription(sku: String): Flow<String>
    fun getSkuPrice(sku: String): Flow<Currency>
    fun getSubscriptionMetaData(sku: String): Flow<PurchaseMetaData?>
    fun getNewPurchases(): SharedFlow<List<String>>
    fun canPurchase(sku: String): Flow<Boolean>
    suspend fun refreshPurchases()

}
