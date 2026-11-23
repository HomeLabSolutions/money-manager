package com.d9tilov.android.insights.domain.model

import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.core.model.TransactionType
import kotlinx.datetime.LocalDate

data class DailyTotal(
    val date: LocalDate,
    val type: TransactionType,
    val currency: String,
    val amount: Decimal,
)
