package com.d9tilov.moneymanager.standing.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.user.data.local.entity.UserDbModel
import java.math.BigDecimal
import java.util.Date

@Entity(
    tableName = "standing",
    foreignKeys = [
        ForeignKey(
            entity = UserDbModel::class,
            parentColumns = ["uid"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class StandingDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "clientId", index = true) val clientId: String,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "type") val type: TransactionType,
    @ColumnInfo(name = "sum") val sum: BigDecimal,
    @ColumnInfo(name = "categoryId") val categoryId: Long,
    @ColumnInfo(name = "createdDate") val createdDate: Date,
    @ColumnInfo(name = "expireDate") val expireDate: Date,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "pushEnable") val pushEnable: Boolean,
    @ColumnInfo(name = "autoAdded") val autoAdded: Boolean
)
