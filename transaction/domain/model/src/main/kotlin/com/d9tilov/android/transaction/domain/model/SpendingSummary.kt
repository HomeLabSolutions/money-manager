package com.d9tilov.android.transaction.domain.model

import com.d9tilov.android.currency.domain.model.CurrencyMetaData
import java.math.BigDecimal

data class SpendingSummary(
    val availableToday: TransactionSpendingTodayModel,
    val spentToday: BigDecimal,
    val spentInPeriod: BigDecimal,
    val currency: CurrencyMetaData,
)
