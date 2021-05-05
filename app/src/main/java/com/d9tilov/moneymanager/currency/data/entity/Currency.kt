package com.d9tilov.moneymanager.currency.data.entity

import com.d9tilov.moneymanager.core.constants.DataConstants
import java.math.BigDecimal

data class Currency(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val code: String,
    val value: BigDecimal
)
