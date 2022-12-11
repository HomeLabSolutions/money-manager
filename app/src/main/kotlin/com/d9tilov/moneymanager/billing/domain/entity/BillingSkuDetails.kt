package com.d9tilov.moneymanager.billing.domain.entity

import com.d9tilov.android.repo.model.Currency

data class BillingSkuDetails(
    val tag: String,
    val offerToken: String,
    val price: com.d9tilov.android.repo.model.Currency
) {
    companion object {
        const val TAG_ANNUAL = "annual-offer"
        const val TAG_QUARTERLY = "quarterly-offer"
    }
}
