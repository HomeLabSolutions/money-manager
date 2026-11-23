package com.d9tilov.android.transaction.domain.model

import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.currency.domain.model.CurrencyMetaData

data class SpendingSummary(
    val availableToday: TransactionSpendingTodayModel,
    val spentToday: Decimal,
    val spentInPeriod: Decimal,
    val currency: CurrencyMetaData,
)
