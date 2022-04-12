package com.d9tilov.moneymanager.base.data.local.db.converters

import android.location.Location
import androidx.room.TypeConverter

object LocationConverter {

    @TypeConverter
    @JvmStatic
    fun fromLocation(location: Location?): String? {
        if (location == null) return null
        return "${location.latitude}:${location.longitude}"
    }

    @TypeConverter
    @JvmStatic
    fun toLocation(value: String?): Location? {
        val ar = value?.split(":")
        if (ar.isNullOrEmpty()) return null
        return Location("").also {
            it.latitude = ar[0].toDouble()
            it.longitude = ar[1].toDouble()
        }
    }
}
