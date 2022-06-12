package com.d9tilov.moneymanager.core.util

import java.util.Currency
import java.util.Locale

object CurrencyUtils {
    private const val ASCII_OFFSET = 0x41
    private const val UNICODE_FLAG_OFFSET = 0x1F1E6

    private val currencyMap = mapOf(
        "USD" to "$",
        "CAD" to "$",
        "EUR" to "€",
        "AED" to "د.إ.‏",
        "AFN" to "؋",
        "ALL" to "Lek",
        "AMD" to "դր",
        "ARS" to "$",
        "AUD" to "$",
        "AZN" to "ман",
        "BAM" to "KM",
        "BDT" to "৳",
        "BGN" to "лв",
        "BHD" to "د.ب.‏",
        "BIF" to "FBu",
        "BND" to "$",
        "BOB" to "Bs",
        "BRL" to "R$",
        "BWP" to "P",
        "BYN" to "руб",
        "BZD" to "$",
        "CDF" to "FrCD",
        "CHF" to "CHF",
        "CLP" to "$",
        "CNY" to "CN¥",
        "COP" to "$",
        "CRC" to "₡",
        "CVE" to "CV$",
        "CZK" to "Kč",
        "DJF" to "Fdj",
        "DKK" to "kr",
        "DOP" to "RD$",
        "DZD" to "د.ج.‏",
        "EEK" to "kr",
        "EGP" to "ج.م.‏",
        "ERN" to "Nfk",
        "ETB" to "Br",
        "GBP" to "£",
        "GEL" to "GEL",
        "GHS" to "GH₵",
        "GNF" to "FG",
        "GTQ" to "Q",
        "HKD" to "$",
        "HNL" to "L",
        "HRK" to "kn",
        "HUF" to "Ft",
        "IDR" to "Rp",
        "ILS" to "₪",
        "INR" to "টকা",
        "IQD" to "د.ع.‏",
        "IRR" to "﷼",
        "ISK" to "kr",
        "JMD" to "$",
        "JOD" to "د.أ.‏",
        "JPY" to "￥",
        "KES" to "Ksh",
        "KHR" to "៛",
        "KMF" to "FC",
        "KRW" to "₩",
        "KWD" to "د.ك.‏",
        "KZT" to "тңг",
        "LBP" to "ل.ل.‏",
        "LKR" to "SL Re",
        "LTL" to "Lt",
        "LVL" to "Ls",
        "LYD" to "د.ل.‏",
        "MAD" to "د.م.‏",
        "MDL" to "MDL",
        "MGA" to "MGA",
        "MKD" to "MKD",
        "MMK" to "K",
        "MOP" to "MOP$",
        "MUR" to "MURs",
        "MXN" to "$",
        "MYR" to "RM",
        "MZN" to "MTn",
        "NAD" to "N$",
        "NGN" to "₦",
        "NIO" to "C$",
        "NOK" to "kr",
        "NPR" to "नेरू",
        "NZD" to "$",
        "OMR" to "ر.ع.‏",
        "PAB" to "B/",
        "PEN" to "S/",
        "PHP" to "₱",
        "PKR" to "₨",
        "PLN" to "zł",
        "PYG" to "₲",
        "QAR" to "ر.ق.‏",
        "RON" to "RON",
        "RSD" to "дин",
        "RUB" to "₽",
        "RWF" to "FR",
        "SAR" to "ر.س.‏",
        "SDG" to "SDG",
        "SEK" to "kr",
        "SGD" to "$",
        "SOS" to "Ssh",
        "SYP" to "ل.س.‏",
        "THB" to "฿",
        "TND" to "د.ت.‏",
        "TOP" to "T$",
        "TRY" to "TL",
        "TTD" to "$",
        "TWD" to "NT$",
        "TZS" to "TSh",
        "UAH" to "₴",
        "UGX" to "USh",
        "UYU" to "$",
        "UZS" to "UZS",
        "VEF" to "Bs.F",
        "VND" to "₫",
        "XAF" to "FCFA",
        "XOF" to "CFA",
        "YER" to "ر.ي.‏",
        "ZAR" to "R",
        "ZMK" to "ZK",
        "ZWL" to "ZWL$"
    )

    fun getCurrencyFullName(code: String): String = Currency.getInstance(code).displayName

    fun getCurrencySignBy(code: String): String =
        Currency.getInstance(code).getSymbol(Locale.getDefault())

    fun getCurrencyIcon(code: String): String {
        val firstChar = Character.codePointAt(code, 0) - ASCII_OFFSET + UNICODE_FLAG_OFFSET
        val secondChar = Character.codePointAt(code, 1) - ASCII_OFFSET + UNICODE_FLAG_OFFSET
        return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
    }

    fun String.getSymbolByCode(): String = currencyMap[this] ?: this
}