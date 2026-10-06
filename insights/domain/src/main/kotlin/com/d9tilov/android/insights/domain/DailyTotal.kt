package com.d9tilov.android.insights.domain

import com.d9tilov.android.core.model.TransactionType
import kotlinx.datetime.LocalDate
import java.math.BigDecimal

data class DailyTotal(
    val date: LocalDate,
    val type: TransactionType,
    val currency: String,
    val amount: BigDecimal,
)
