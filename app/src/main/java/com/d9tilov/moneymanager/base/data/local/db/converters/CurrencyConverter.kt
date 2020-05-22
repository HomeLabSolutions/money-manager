package com.d9tilov.moneymanager.base.data.local.db.converters

import androidx.room.TypeConverter
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.Companion.DECIMAL_LENGTH
import java.math.BigDecimal
import java.math.RoundingMode

object CurrencyConverter {

    @TypeConverter
    @JvmStatic
    fun fromBigDecimalToLong(value: BigDecimal): Long {
        return (value * 10.toBigDecimal().pow(DECIMAL_LENGTH)).toLong()
    }

    @TypeConverter
    @JvmStatic
    fun fromLongToBigDecimal(value: Long): BigDecimal {
        var result = value.toBigDecimal()
            .divide(10.toBigDecimal().pow(DECIMAL_LENGTH), DECIMAL_LENGTH, RoundingMode.HALF_UP)
        result = result.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros()
        if (result.scale() < 0) {
            result = result.setScale(0)
        }
        return result
    }
}
