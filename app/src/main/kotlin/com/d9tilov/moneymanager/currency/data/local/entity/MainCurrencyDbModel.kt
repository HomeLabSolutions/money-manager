package com.d9tilov.moneymanager.currency.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.d9tilov.moneymanager.user.data.entity.UserDbModel

@Entity(
    tableName = "main_currency",
    foreignKeys = [
        ForeignKey(
            entity = UserDbModel::class,
            parentColumns = ["uid"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MainCurrencyDbModel(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int = 1,
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "clientId", index = true) val clientId: String
)
