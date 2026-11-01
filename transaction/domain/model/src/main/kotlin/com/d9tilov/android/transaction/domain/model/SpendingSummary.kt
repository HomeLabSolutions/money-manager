package com.d9tilov.android.transaction.domain.model

import java.math.BigDecimal

data class SpendingSummary(
    val availableToday: TransactionSpendingTodayModel,
    val spentToday: BigDecimal,
    val spentInPeriod: BigDecimal,
)
