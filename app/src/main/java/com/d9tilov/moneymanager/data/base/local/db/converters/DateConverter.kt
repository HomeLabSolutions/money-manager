package com.d9tilov.moneymanager.data.base.local.db.converters

import androidx.room.TypeConverter
import java.util.*

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
