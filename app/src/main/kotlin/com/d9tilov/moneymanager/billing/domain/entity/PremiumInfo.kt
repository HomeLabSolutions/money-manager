package com.d9tilov.moneymanager.billing.domain.entity

import com.d9tilov.moneymanager.currency.domain.entity.DomainCurrency

data class PremiumInfo(
    val canPurchase: Boolean,
    val isPremium: Boolean,
    val hasActiveSku: Boolean,
    val minBillingPrice: DomainCurrency
)
