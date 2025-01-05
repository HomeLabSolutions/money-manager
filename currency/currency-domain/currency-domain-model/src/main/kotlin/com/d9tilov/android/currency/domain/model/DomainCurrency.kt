package com.d9tilov.android.currency.domain.model

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_SYMBOL
import java.math.BigDecimal

data class DomainCurrency(
    val code: String,
    val symbol: String,
    val value: BigDecimal,
    val isBase: Boolean = false,
    val lastUpdateTime: Long,
) {
    companion object {
        val EMPTY =
            DomainCurrency(
                code = DEFAULT_CURRENCY_CODE,
                symbol = DEFAULT_CURRENCY_SYMBOL,
                value = BigDecimal.ZERO,
                isBase = false,
                lastUpdateTime = System.currentTimeMillis(),
            )
    }
}
