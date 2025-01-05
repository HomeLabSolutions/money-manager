package com.d9tilov.android.core.utils

object MainPriceFieldParser {
    const val MAX_PRICE_LENGTH = 11

    fun parse(
        priceStr: String,
        btn: KeyPress,
    ): String =
        if (btn == KeyPress.Del) {
            if (priceStr.length == 1) {
                KeyPress.Zero.value
            } else {
                priceStr.dropLast(1)
            }
        } else {
            if (priceStr.length == MAX_PRICE_LENGTH) {
                priceStr
            } else {
                if (btn == KeyPress.Dot) {
                    if (priceStr.contains(KeyPress.Dot.value)) {
                        priceStr
                    } else {
                        priceStr + btn.value
                    }
                } else {
                    if (priceStr.length == 1 && priceStr == KeyPress.Zero.value) {
                        btn.value
                    } else if (priceStr.contains(KeyPress.Dot.value)) {
                        if (priceStr.substringAfterLast(KeyPress.Dot.value).length == 2) {
                            priceStr
                        } else {
                            priceStr + btn.value
                        }
                    } else {
                        priceStr + btn.value
                    }
                }
            }
        }

    fun isInputValid(str: String): Boolean {
        if (str.isEmpty()) return false
        if (str.length > MAX_PRICE_LENGTH) {
            return false
        } else {
            if (str[0] == '.') return false
            if (str[0] == '0' && str.length == 1) return false
            if (str.length == 2 && str[0] == '0' && str[1] == '.') return false
            if (str.length > 1 && str[0] == '0' && str[1] != '.') return false
        }
        return try {
            str.toBigDecimal()
            true
        } catch (ex: NumberFormatException) {
            false
        }
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
