package com.d9tilov.moneymanager.billing.domain.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.currency.data.entity.Currency
import kotlinx.parcelize.Parcelize

@Parcelize
data class BillingSkuDetails(
    val tag: String,
    val offerToken: String,
    val price: Currency
) : Parcelable {
    companion object {
        const val TAG_ANNUAL = "annual-offer"
        const val TAG_QUARTERLY = "quarterly-offer"
    }
}
