package com.d9tilov.android.core.utils

import com.d9tilov.android.core.constants.CurrencyConstants.DECIMAL_LENGTH
import com.d9tilov.android.core.constants.CurrencyConstants.DECIMAL_SEPARATOR
import com.d9tilov.android.core.constants.CurrencyConstants.DEFAULT_DECIMAL_SEPARATOR
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val String?.toBigDecimal: BigDecimal
    get() {
        val df = DecimalFormat()
        df.isParseBigDecimal = true
        if (this.isNullOrEmpty()) {
            return BigDecimal.ZERO
        }
        var result: BigDecimal
        result = try {
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

val BigDecimal?.removeScale: BigDecimal
    get() {
        if (this == null || this == BigDecimal.ZERO) {
            return BigDecimal.ZERO
        }
        val newNum = this.setScale(DECIMAL_LENGTH, RoundingMode.HALF_UP).stripTrailingZeros()
        if (newNum.signum() == 0) return BigDecimal.ZERO
        return newNum
    }