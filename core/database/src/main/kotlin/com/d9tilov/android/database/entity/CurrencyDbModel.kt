package com.d9tilov.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.d9tilov.android.core.model.Decimal

@Entity(tableName = "currency")
data class CurrencyDbModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "symbol") val symbol: String,
    @ColumnInfo(name = "value") val value: Decimal,
    @ColumnInfo(name = "lastUpdateTime") val lastUpdateTime: Long,
)
