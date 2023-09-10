package com.d9tilov.android.billing.domain.model

import com.d9tilov.android.currency.domain.model.DomainCurrency

data class PremiumInfo(
    val canPurchase: Boolean,
    val isPremium: Boolean,
    val hasActiveSku: Boolean,
    val minBillingPrice: DomainCurrency
)
