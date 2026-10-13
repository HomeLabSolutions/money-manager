package com.d9tilov.android.database.dao

import java.math.BigDecimal

data class TransactionSearchQuery(
    val amountQuery: BigDecimal?,
    val textPattern: String,
)
