package com.d9tilov.android.transaction.domain.model

import java.math.BigDecimal

data class TransactionLineChartModel(
    val currencyCode: String,
    val sum: BigDecimal
)
