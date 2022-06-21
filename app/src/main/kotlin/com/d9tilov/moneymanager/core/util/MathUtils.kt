package com.d9tilov.moneymanager.core.util // ktlint-disable filename

import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.LONG_DECIMAL_LENGTH
import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal?.divideBy(divider: BigDecimal): BigDecimal {
    var result = this?.let { divide(divider, LONG_DECIMAL_LENGTH, RoundingMode.HALF_UP) }
        ?: BigDecimal.ZERO
    result = result.stripTrailingZeros()
    if (result.scale() < 0) {
        result = result.setScale(0)
    }
    return result
}
