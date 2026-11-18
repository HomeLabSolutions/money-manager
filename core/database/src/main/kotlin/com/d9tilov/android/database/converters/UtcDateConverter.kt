package com.d9tilov.android.database.converters

import androidx.room.TypeConverter
import com.d9tilov.android.core.utils.toLocalDateTime
import com.d9tilov.android.core.utils.toMillis
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

object UtcDateConverter {
    @TypeConverter
    @JvmStatic
    fun toDateTime(value: Long): LocalDateTime = value.toLocalDateTime(TimeZone.UTC)

    @TypeConverter
    @JvmStatic
    fun fromDateTime(date: LocalDateTime): Long = date.toMillis(TimeZone.UTC)
}
