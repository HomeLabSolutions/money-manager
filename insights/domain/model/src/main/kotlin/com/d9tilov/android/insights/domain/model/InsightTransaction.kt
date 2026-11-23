package com.d9tilov.android.insights.domain.model

import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.core.model.TransactionType
import kotlinx.datetime.LocalDateTime

data class InsightTransaction(
    val type: TransactionType,
    val date: LocalDateTime,
    val category: String,
    val currency: String,
    val amount: Decimal,
)
