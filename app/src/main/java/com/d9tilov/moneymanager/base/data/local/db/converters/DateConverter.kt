package com.d9tilov.moneymanager.base.data.local.db.converters

import androidx.room.TypeConverter
import com.d9tilov.moneymanager.core.util.toLocal
import com.d9tilov.moneymanager.core.util.toMillis
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime

object DateConverter {

    @TypeConverter
    @JvmStatic
    fun toOffsetDateTime(value: Long): LocalDateTime =
        Instant.fromEpochMilliseconds(value).toLocal()

    @TypeConverter
    @JvmStatic
    fun fromOffsetDateTime(date: LocalDateTime): Long = date.toMillis()
}
