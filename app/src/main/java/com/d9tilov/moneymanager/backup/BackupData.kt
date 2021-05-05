package com.d9tilov.moneymanager.backup

import android.os.Parcelable
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BackupData(
    val lastBackupTimestamp: Long = 0L,
    val dbVersion: Int = AppDatabase.VERSION_NUMBER
) : Parcelable
