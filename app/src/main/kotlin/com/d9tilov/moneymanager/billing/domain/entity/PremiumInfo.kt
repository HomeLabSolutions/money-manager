package com.d9tilov.moneymanager.billing.domain.entity

import com.d9tilov.android.interactor.model.DomainCurrency

data class PremiumInfo(
    val canPurchase: Boolean,
    val isPremium: Boolean,
    val hasActiveSku: Boolean,
    val minBillingPrice: com.d9tilov.android.interactor.model.DomainCurrency
)
