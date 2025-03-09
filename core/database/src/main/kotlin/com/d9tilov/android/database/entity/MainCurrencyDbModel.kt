package com.d9tilov.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "main_currency",
    foreignKeys = [
        ForeignKey(
            entity = UserDbModel::class,
            parentColumns = ["uid"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class MainCurrencyDbModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 1,
    @ColumnInfo(name = "clientId", index = true) val clientId: String,
    @ColumnInfo(name = "code") val code: String,
)
