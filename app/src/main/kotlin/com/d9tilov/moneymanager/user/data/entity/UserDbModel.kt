package com.d9tilov.moneymanager.user.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserDbModel(

    @PrimaryKey @ColumnInfo(name = "uid") var uid: String,
    @ColumnInfo(name = "firstName") val firstName: String?,
    @ColumnInfo(name = "lastName") val lastName: String?,
    @ColumnInfo(name = "currentCurrencyCode") val currentCurrencyCode: String,
    @ColumnInfo(name = "showPrepopulate") val showPrepopulate: Boolean,
    @ColumnInfo(name = "fiscalDay") val fiscalDay: Int
)
