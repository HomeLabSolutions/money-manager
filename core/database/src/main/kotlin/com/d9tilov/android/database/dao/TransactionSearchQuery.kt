package com.d9tilov.android.database.dao

import com.d9tilov.android.core.model.Decimal

data class TransactionSearchQuery(
    val amountQuery: Decimal?,
    val textPattern: String,
)
