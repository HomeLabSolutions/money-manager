package com.d9tilov.moneymanager.base.data.local.db.converters

import androidx.room.TypeConverter
import java.util.Date

object DateConverter {
    @TypeConverter
    @JvmStatic
    fun fromDateToLong(value: Date): Long {
        return value.time
    }

    @TypeConverter
    @JvmStatic
    fun fromLongToDate(value: Long): Date {
        return Date(value)
    }
}
