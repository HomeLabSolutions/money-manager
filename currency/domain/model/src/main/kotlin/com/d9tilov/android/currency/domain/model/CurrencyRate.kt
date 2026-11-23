package com.d9tilov.android.currency.domain.model

import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_CODE
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_CURRENCY_SYMBOL
import com.d9tilov.android.core.model.Decimal

data class CurrencyRate(
    val code: String = DEFAULT_CURRENCY_CODE,
    val symbol: String,
    val value: Decimal,
    val lastUpdateTime: Long,
) {
    companion object {
        val EMPTY =
            CurrencyRate(
                code = DEFAULT_CURRENCY_CODE,
                symbol = DEFAULT_CURRENCY_SYMBOL,
                value = Decimal.ZERO,
                lastUpdateTime = System.currentTimeMillis(),
            )
    }
}
