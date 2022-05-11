package com.d9tilov.moneymanager.billing.domain.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.currency.data.entity.Currency
import kotlinx.parcelize.Parcelize

@Parcelize
data class BillingSkuDetails(
    val type: String,
    val title: String,
    val description: String,
    val price: Currency,
    val isPurchased: Boolean,
    val metaData: PurchaseMetaData?
) : Parcelable {
    companion object {
        const val SKU_SUBSCRIPTION_QUARTERLY = "premium_quarterly"
        const val SKU_SUBSCRIPTION_ANNUAL = "premium_annual"
    }
}
