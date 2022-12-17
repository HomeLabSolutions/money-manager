package com.d9tilov.android.currency.data.contract.model

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_SYMBOL
import java.math.BigDecimal

data class Currency(
    val code: String = DEFAULT_CURRENCY_CODE,
    val symbol: String,
    val value: BigDecimal,
    val lastUpdateTime: Long
) {
    companion object {
        val EMPTY = Currency(
            code = DEFAULT_CURRENCY_CODE,
            symbol = DEFAULT_CURRENCY_SYMBOL,
            value = BigDecimal.ZERO,
            lastUpdateTime = System.currentTimeMillis()
        )
    }
}
