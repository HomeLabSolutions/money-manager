package com.d9tilov.android.core.utils

object MainPriceFieldParser {
    const val MAX_PRICE_LENGTH = 11
    private const val MAX_FRACTION_DIGITS = 2

    fun parse(
        priceStr: String,
        btn: KeyPress,
        precision: Int = MAX_FRACTION_DIGITS,
    ): String =
        when {
            btn == KeyPress.Del -> {
                priceStr.dropLast(1).ifEmpty { KeyPress.Zero.value }
            }

            priceStr.length >= MAX_PRICE_LENGTH -> {
                priceStr
            }

            btn == KeyPress.Dot -> {
                if (precision == 0 || priceStr.contains(KeyPress.Dot.value)) priceStr else priceStr + btn.value
            }

            priceStr == KeyPress.Zero.value -> {
                btn.value
            }

            hasMaxFractionDigits(priceStr, precision) -> {
                priceStr
            }

            else -> {
                priceStr + btn.value
            }
        }

    fun isInputValid(str: String): Boolean =
        str.length in 1..MAX_PRICE_LENGTH && hasValidPrefix(str) && str.toBigDecimalOrNull() != null

    fun allowsAmountInput(
        text: String,
        precision: Int,
    ): Boolean {
        val normalized = text.removePrefix("-").replace(',', '.')
        val parts = normalized.split('.')
        val fractionAllowed = parts.size == 2 && precision > 0 && parts.last().length <= precision
        val validPrecision = parts.size == 1 || fractionAllowed
        return validPrecision && parts.all { part -> part.all { it in '0'..'9' } }
    }

    private fun hasMaxFractionDigits(
        str: String,
        precision: Int,
    ): Boolean = str.contains(KeyPress.Dot.value) && str.substringAfterLast(KeyPress.Dot.value).length >= precision

    private fun hasValidPrefix(str: String): Boolean =
        when {
            str.startsWith(KeyPress.Dot.value) -> false
            str == KeyPress.Zero.value || str == KeyPress.Zero.value + KeyPress.Dot.value -> false
            str.startsWith(KeyPress.Zero.value) -> str.startsWith(KeyPress.Zero.value + KeyPress.Dot.value)
            else -> true
        }
}

enum class KeyPress(
    val value: String,
) {
    One("1"),
    Two("2"),
    Three("3"),
    Four("4"),
    Five("5"),
    Six("6"),
    Seven("7"),
    Eight("8"),
    Nine("9"),
    Dot("."),
    Zero("0"),
    Del("del"),
}

fun String.toKeyPress(): KeyPress? = KeyPress.entries.firstOrNull { it.value == this }
