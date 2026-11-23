package com.d9tilov.android.database.converters

import androidx.room.TypeConverter
import com.d9tilov.android.core.constants.CurrencyConstants.DECIMAL_LENGTH
import com.d9tilov.android.core.model.Decimal

object CurrencyConverter {
    private val storageFactor = Decimal.ONE.movePoint(DECIMAL_LENGTH)

    @TypeConverter
    @JvmStatic
    fun fromDecimalToString(value: Decimal): String = (value * storageFactor).toPlainString()

    @TypeConverter
    @JvmStatic
    fun fromStringToDecimal(value: String): Decimal =
        Decimal(value).movePoint(-DECIMAL_LENGTH).withoutTrailingFractionZeros()
}
