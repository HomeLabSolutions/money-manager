package com.d9tilov.android.core.model

/** [value] is an exact count of the currency's minor units. */
data class Money(
    val value: Long,
    val currency: Currency,
) {
    fun toDecimal(): Decimal = Decimal(value).movePoint(-currency.precision).withoutTrailingFractionZeros()

    fun toAmountString(): String = toDecimal().toPlainString()
}
