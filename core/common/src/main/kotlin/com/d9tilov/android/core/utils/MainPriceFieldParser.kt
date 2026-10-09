package com.d9tilov.android.core.utils

object MainPriceFieldParser {
    const val MAX_PRICE_LENGTH = 11
    private const val MAX_FRACTION_DIGITS = 2

    fun parse(
        priceStr: String,
        btn: KeyPress,
    ): String =
        when {
            btn == KeyPress.Del -> {
                priceStr.dropLast(1).ifEmpty { KeyPress.Zero.value }
            }

            priceStr.length >= MAX_PRICE_LENGTH -> {
                priceStr
            }

            btn == KeyPress.Dot -> {
                if (priceStr.contains(KeyPress.Dot.value)) priceStr else priceStr + btn.value
            }

            priceStr == KeyPress.Zero.value -> {
                btn.value
            }

            hasMaxFractionDigits(priceStr) -> {
                priceStr
            }

            else -> {
                priceStr + btn.value
            }
        }

    fun isInputValid(str: String): Boolean =
        str.length in 1..MAX_PRICE_LENGTH && hasValidPrefix(str) && str.toBigDecimalOrNull() != null

    private fun hasMaxFractionDigits(str: String): Boolean =
        str.contains(KeyPress.Dot.value) && str.substringAfterLast(KeyPress.Dot.value).length >= MAX_FRACTION_DIGITS

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
