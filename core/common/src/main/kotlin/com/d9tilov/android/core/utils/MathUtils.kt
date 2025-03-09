@file:Suppress("ktlint:standard:filename")

package com.d9tilov.android.core.utils

import com.d9tilov.android.core.constants.CurrencyConstants.LONG_DECIMAL_LENGTH
import java.math.BigDecimal
import java.math.RoundingMode

fun BigDecimal?.divideBy(divider: BigDecimal): BigDecimal {
    var result =
        this?.let { divide(divider, LONG_DECIMAL_LENGTH, RoundingMode.HALF_UP) }
            ?: BigDecimal.ZERO
    result = result.stripTrailingZeros()
    if (result.scale() < 0) {
        result = result.setScale(0)
    }
    return result
}
