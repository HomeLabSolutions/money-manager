package com.d9tilov.moneymanager.user.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.d9tilov.moneymanager.backup.BackupData

@Entity(tableName = "users")
data class UserDbModel(

    @PrimaryKey @ColumnInfo(name = "uid") var uid: String,
    @ColumnInfo(name = "firstName") val firstName: String?,
    @ColumnInfo(name = "lastName") val lastName: String?,
    @ColumnInfo(name = "currencyCode") val currencyCode: String,
    @ColumnInfo(name = "showPrepopulate") val showPrepopulate: Boolean,
    @ColumnInfo(name = "backupData") val backupData: BackupData,
)
