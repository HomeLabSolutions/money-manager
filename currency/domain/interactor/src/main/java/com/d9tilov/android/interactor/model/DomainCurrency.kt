package com.d9tilov.android.interactor.model

import com.d9tilov.android.core.constants.DataConstants
import java.math.BigDecimal

data class DomainCurrency(
    val code: String,
    val symbol: String,
    val value: BigDecimal,
    val isBase: Boolean = false,
    val lastUpdateTime: Long
) {

    companion object {
        val EMPTY = DomainCurrency(
            code = DataConstants.DEFAULT_CURRENCY_CODE,
            symbol = DataConstants.DEFAULT_CURRENCY_SYMBOL,
            value = BigDecimal.ZERO,
            isBase = false,
            lastUpdateTime = System.currentTimeMillis()
        )
    }
}
