package com.d9tilov.android.core.utils

object MainPriceFieldParser {

    fun parse(priceStr: String, btn: KeyPress): String {
        return if (btn == KeyPress.Del) {
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
    }

    private const val MAX_PRICE_LENGTH = 11
}

enum class KeyPress(val value: String) {
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
    Del("del")
}

fun String.toKeyPress(): KeyPress? = KeyPress.values().firstOrNull { it.value == this }
