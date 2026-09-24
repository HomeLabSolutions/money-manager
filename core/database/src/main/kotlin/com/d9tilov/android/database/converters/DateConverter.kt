package com.d9tilov.android.database.converters

import androidx.room.TypeConverter
import com.d9tilov.android.core.utils.toLocalDateTime
import com.d9tilov.android.core.utils.toMillis
import kotlinx.datetime.LocalDateTime

object DateConverter {
    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: Long): LocalDateTime = value.toLocalDateTime()

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: LocalDateTime): Long = date.toMillis()
}
