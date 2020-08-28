package com.d9tilov.moneymanager.prepopulate.currency.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "currency")
data class CurrencyDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "value") val value: BigDecimal
)
