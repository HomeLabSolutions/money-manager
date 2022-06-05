package com.d9tilov.moneymanager.base.data.local.db.converters

import androidx.room.TypeConverter
import com.d9tilov.moneymanager.core.ui.widget.currencyview.CurrencyConstants.DECIMAL_LENGTH
import java.math.BigDecimal

object CurrencyConverter {

    @TypeConverter
    @JvmStatic
    fun fromBigDecimalToString(value: BigDecimal): String {
        return (value * 10.toBigDecimal().pow(DECIMAL_LENGTH)).toString()
    }

    @TypeConverter
    @JvmStatic
    fun fromLongToBigDecimal(value: String): BigDecimal {
        var result = value.toBigDecimal().divide(10.toBigDecimal().pow(DECIMAL_LENGTH)).stripTrailingZeros()
        if (result.scale() < 0) {
            result = result.setScale(0)
        }
        return result
    }
}
