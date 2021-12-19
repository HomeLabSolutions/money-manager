package com.d9tilov.moneymanager.transaction.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.d9tilov.moneymanager.transaction.TransactionType
import com.d9tilov.moneymanager.user.data.local.entity.UserDbModel
import java.math.BigDecimal
import java.util.Date

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = UserDbModel::class,
            parentColumns = ["uid"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TransactionDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "clientId", index = true) val clientId: String,
    @ColumnInfo(name = "type") val type: TransactionType,
    @ColumnInfo(name = "categoryId") val categoryId: Long,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "sum") val sum: BigDecimal,
    @ColumnInfo(name = "usd_sum") val usdSum: BigDecimal,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "qrCode") val qrCode: String?,
    @ColumnInfo(name = "isDate") val isDate: Boolean,
    @ColumnInfo(name = "isRegular") val isRegular: Boolean
)
