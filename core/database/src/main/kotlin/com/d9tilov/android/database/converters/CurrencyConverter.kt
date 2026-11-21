package com.d9tilov.android.database.converters

import androidx.room.TypeConverter
import java.math.BigDecimal

object CurrencyConverter {
    internal const val STORAGE_SCALE = 2

    @TypeConverter
    @JvmStatic
    fun fromBigDecimalToString(value: BigDecimal): String = (value * 10.toBigDecimal().pow(STORAGE_SCALE)).toString()

    @TypeConverter
    @JvmStatic
    fun fromLongToBigDecimal(value: String): BigDecimal {
        var result = value.toBigDecimal().divide(10.toBigDecimal().pow(STORAGE_SCALE)).stripTrailingZeros()
        if (result.scale() < 0) {
            result = result.setScale(0)
        }
        return result
    }
}
