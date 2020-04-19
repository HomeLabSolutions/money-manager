package com.d9tilov.moneymanager.data.user.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserDbModel(

    @PrimaryKey @ColumnInfo(name = "uid") var uid: String,
    @ColumnInfo(name = "userName") val userName: String? = "",
    @ColumnInfo(name = "firstName") val firstName: String? = "",
    @ColumnInfo(name = "lastName") val lastName: String? = "",
    @ColumnInfo(name = "pushSound") val pushSound: String? = "",
    @ColumnInfo(name = "pushReportEnabled") val pushReportEnabled: Boolean = true,
    @ColumnInfo(name = "hideTransactionDetails") val hideTransactionDetails: Boolean = true,
    @ColumnInfo(name = "fiscalDay") val fiscalDay: Int = 0,
    @ColumnInfo(name = "budgetDayCreation") val budgetDayCreation: Long

)
