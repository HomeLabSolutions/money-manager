package com.d9tilov.moneymanager.backup.data.entity

import android.os.Parcelable
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import kotlinx.parcelize.Parcelize

@Parcelize
data class BackupData(
    val lastBackupTimestamp: Long,
    val dbVersion: Int
) : Parcelable {

    companion object {
        const val UNKNOWN_BACKUP_DATA = -1L
        val EMPTY = BackupData(
            lastBackupTimestamp = UNKNOWN_BACKUP_DATA,
            dbVersion = AppDatabase.VERSION_NUMBER
        )
    }
}
