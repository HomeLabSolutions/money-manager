package com.d9tilov.android.core.utils

import com.d9tilov.android.core.model.Currency
import com.d9tilov.android.core.model.Money
import java.math.BigDecimal

fun Money.toDecimal(): BigDecimal = BigDecimal.valueOf(value, currency.precision).withoutTrailingZeros()

fun BigDecimal.withoutTrailingZeros(): BigDecimal {
    val amount = stripTrailingZeros()
    return if (amount.scale() < 0) amount.setScale(0) else amount
}

fun Money.toAmountString(): String = toDecimal().toPlainString()

fun BigDecimal.toMoneyOrNull(currency: Currency): Money? =
    try {
        Money(movePointRight(currency.precision).longValueExact(), currency)
    } catch (_: ArithmeticException) {
        null
    }

fun BigDecimal.toMoney(currencyCode: String): Money =
    checkNotNull(Currency.fromCode(currencyCode)?.let(::toMoneyOrNull)) {
        "Amount cannot be represented in minor units of $currencyCode"
    }

fun String.moneyOrNull(currency: Currency): Money? = replace(',', '.').toBigDecimalOrNull()?.toMoneyOrNull(currency)

fun String.amountAfterCurrencyChange(currency: Currency): String = moneyOrNull(currency)?.toAmountString() ?: "0"
