package com.d9tilov.moneymanager.currency.domain.entity

import com.d9tilov.moneymanager.core.constants.DataConstants
import java.math.BigDecimal

data class DomainCurrency(
    val id: Long = DataConstants.DEFAULT_DATA_ID,
    val code: String,
    val symbol: String,
    val value: BigDecimal,
    val isBase: Boolean = false,
    val used: Boolean,
    val lastTimeUpdate: Long
)
