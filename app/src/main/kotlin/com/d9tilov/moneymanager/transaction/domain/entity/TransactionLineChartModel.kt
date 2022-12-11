package com.d9tilov.moneymanager.transaction.domain.entity

import java.math.BigDecimal

data class TransactionLineChartModel(
    val currencyCode: String,
    val sum: BigDecimal
)
