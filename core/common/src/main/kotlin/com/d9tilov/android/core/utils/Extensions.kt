package com.d9tilov.android.core.utils

import com.d9tilov.android.core.constants.CurrencyConstants.DECIMAL_SEPARATOR
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_DECIMAL_SEPARATOR
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

val String?.toBigDecimal: BigDecimal
    get() {
        val df = DecimalFormat()
        df.isParseBigDecimal = true
        if (this.isNullOrEmpty()) {
            return BigDecimal.ZERO
        }
        var result: BigDecimal
        result =
            try {
                replace("\\s".toRegex(), "")
                    .replace(DECIMAL_SEPARATOR, DEFAULT_DECIMAL_SEPARATOR)
                    .toBigDecimal()
            } catch (ex: NumberFormatException) {
                BigDecimal.ZERO
            }
        result = result.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros()
        if (result.scale() < 0) {
            result = result.setScale(0)
        }
        return result
    }

fun BigDecimal?.reduceScale(): BigDecimal =
    this
        ?.setScale(2, RoundingMode.HALF_UP)
        ?.stripTrailingZeros() ?: BigDecimal.ZERO

fun BigDecimal?.reduceScaleStr(): String = this.reduceScale().toPlainString()
