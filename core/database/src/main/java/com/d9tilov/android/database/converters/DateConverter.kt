package com.d9tilov.android.database.converters

import androidx.room.TypeConverter
import com.d9tilov.android.common_android.utils.fromUTCToLocalDateTime
import com.d9tilov.android.common_android.utils.toMillis
import com.d9tilov.android.common_android.utils.toUTC
import kotlinx.datetime.LocalDateTime

object DateConverter {

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: Long): LocalDateTime = value.fromUTCToLocalDateTime()

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: LocalDateTime): Long = date.toUTC().toMillis()
}
