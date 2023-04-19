package com.d9tilov.android.billing.data.model

import com.d9tilov.android.currency.data.model.Currency

data class BillingSkuDetails(
    val tag: String,
    val offerToken: String,
    val price: Currency
) {
    companion object {
        const val TAG_ANNUAL = "annual-offer"
        const val TAG_QUARTERLY = "quarterly-offer"
    }
}
