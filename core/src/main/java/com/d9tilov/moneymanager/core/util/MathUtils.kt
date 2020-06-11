package com.d9tilov.moneymanager.core.util

import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.Companion.DECIMAL_LENGTH
import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal?.divideBy(divider: BigDecimal): BigDecimal {
    var result = this?.let { divide(divider, DECIMAL_LENGTH, RoundingMode.HALF_UP) }
        ?: BigDecimal.ZERO
    result = result.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros()
    if (result.scale() < 0) {
        result = result.setScale(0)
    }
    return result
}
