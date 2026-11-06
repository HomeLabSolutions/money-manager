package com.d9tilov.android.billing.domain.contract

import com.d9tilov.android.billing.domain.model.BillingSkuDetails
import com.d9tilov.android.billing.domain.model.PremiumInfo
import kotlinx.coroutines.flow.Flow

interface BillingInteractor {
    val isNewPurchaseAcknowledged: Flow<Boolean>
    val billingConnectionReady: Flow<Boolean>

    fun getPremiumInfo(): Flow<PremiumInfo>

    fun startBillingConnection()

    fun terminateBillingConnection()

    fun isPremium(): Flow<Boolean>

    fun getSkuDetails(): Flow<List<BillingSkuDetails>>
}
