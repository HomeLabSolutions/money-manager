package com.d9tilov.moneymanager.base.data.local.db.converters

import androidx.room.TypeConverter
import com.d9tilov.moneymanager.core.util.fromUTCToLocalDateTime
import com.d9tilov.moneymanager.core.util.toMillis
import com.d9tilov.moneymanager.core.util.toUTC
import kotlinx.datetime.LocalDateTime

object DateConverter {

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: Long): LocalDateTime = value.fromUTCToLocalDateTime()

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: LocalDateTime): Long = date.toUTC().toMillis()
}
