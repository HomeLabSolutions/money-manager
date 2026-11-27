package com.d9tilov.android.core.utils

import com.d9tilov.android.core.model.Currency
import com.d9tilov.android.core.model.Decimal
import com.d9tilov.android.core.model.Money

fun String.toDecimalOrNull(): Decimal? =
    try {
        Decimal(this)
    } catch (_: NumberFormatException) {
        null
    }

fun String.moneyOrNull(currency: Currency): Money? = replace(',', '.').toDecimalOrNull()?.toMoney(currency)

fun String.toDecimal(): Decimal = Decimal(this)

fun Int.toDecimal(): Decimal = Decimal(this)

fun Long.toDecimal(): Decimal = Decimal(this)

fun Decimal?.roundToCurrency(currencyCode: String): Decimal {
    val amount = this ?: Decimal.ZERO
    val currency = Currency.fromCode(currencyCode) ?: return amount
    return amount.roundTo(currency.precision)
}

fun Decimal?.toAmountString(currencyCode: String): String =
    roundToCurrency(currencyCode).withoutTrailingFractionZeros().toPlainString()

fun <T> Iterable<T>.sumOfDecimals(selector: (T) -> Decimal): Decimal =
    fold(Decimal.ZERO) { sum, item -> sum + selector(item) }

fun Decimal.toDouble(): Double = toPlainString().toDouble()

fun Decimal.toFloat(): Float = toPlainString().toFloat()
