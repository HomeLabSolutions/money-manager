package com.d9tilov.android.core.utils

import com.d9tilov.android.core.constants.CurrencyConstants.DECIMAL_LENGTH
import com.d9tilov.android.core.model.Decimal

fun String.toDecimalOrNull(): Decimal? =
    try {
        Decimal(this)
    } catch (_: NumberFormatException) {
        null
    }

fun String.toDecimal(): Decimal = Decimal(this)

fun Int.toDecimal(): Decimal = Decimal(this)

fun Long.toDecimal(): Decimal = Decimal(this)

fun Decimal?.reduceScale(): Decimal = (this ?: Decimal.ZERO).roundTo(DECIMAL_LENGTH)

fun Decimal?.reduceScaleStr(): String = reduceScale().withoutTrailingFractionZeros().toPlainString()

fun <T> Iterable<T>.sumOfDecimals(selector: (T) -> Decimal): Decimal =
    fold(Decimal.ZERO) { sum, item -> sum + selector(item) }

fun Decimal.toDouble(): Double = toPlainString().toDouble()

fun Decimal.toFloat(): Float = toPlainString().toFloat()
