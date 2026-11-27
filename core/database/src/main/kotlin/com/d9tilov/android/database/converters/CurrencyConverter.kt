package com.d9tilov.android.database.converters

import androidx.room.TypeConverter
import com.d9tilov.android.core.model.Decimal

object CurrencyConverter {
    // The existing TEXT columns encode each decimal by shifting it two places.
    private const val TEXT_ENCODING_SHIFT = 2

    private val storageFactor = Decimal.ONE.movePoint(TEXT_ENCODING_SHIFT)

    @TypeConverter
    @JvmStatic
    fun fromDecimalToString(value: Decimal): String = (value * storageFactor).toPlainString()

    @TypeConverter
    @JvmStatic
    fun fromStringToDecimal(value: String): Decimal =
        Decimal(value).movePoint(-TEXT_ENCODING_SHIFT).withoutTrailingFractionZeros()
}
