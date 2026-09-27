package com.d9tilov.android.insights.data

import com.d9tilov.android.core.model.TransactionType
import kotlinx.datetime.LocalDateTime
import java.math.BigDecimal

internal data class InsightTransaction(
    val type: TransactionType,
    val date: LocalDateTime,
    val category: String,
    val currency: String,
    val amount: BigDecimal,
)
