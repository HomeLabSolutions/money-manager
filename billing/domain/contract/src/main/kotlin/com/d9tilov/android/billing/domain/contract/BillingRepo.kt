package com.d9tilov.android.billing.domain.contract

import com.d9tilov.android.billing.domain.model.BillingSkuDetails
import kotlinx.coroutines.flow.Flow

interface BillingRepo {
    val isNewPurchaseAcknowledged: Flow<Boolean>
    val hasRenewablePremium: Flow<Boolean>
    val hasPremiumPurchase: Flow<Boolean>
    val isPremiumEmail: Flow<Boolean>
    val billingConnectionReady: Flow<Boolean>

    fun startBillingConnection()

    fun terminateBillingConnection()

    fun getSkuDetails(): Flow<List<BillingSkuDetails>>
}
