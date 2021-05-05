package com.d9tilov.moneymanager.backup

import androidx.room.TypeConverter

object BackupDataConverter {

    @TypeConverter
    @JvmStatic
    fun fromBackupData(data: BackupData): String {
        return "${data.lastBackupTimestamp}:${data.dbVersion}"
    }

    @TypeConverter
    @JvmStatic
    fun toBackupData(value: String): BackupData {
        val ar = value.split(":")
        return BackupData(ar[0].toLong(), ar[1].toInt())
    }
}
