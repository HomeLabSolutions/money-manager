package com.d9tilov.moneymanager.budget.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.d9tilov.moneymanager.user.data.local.entity.UserDbModel
import java.math.BigDecimal
import java.util.Date

@Entity(
    tableName = "budget",
    foreignKeys = [
        ForeignKey(
            entity = UserDbModel::class,
            parentColumns = ["uid"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BudgetDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "clientId", index = true) val clientId: String,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "sum") val sum: BigDecimal,
    @ColumnInfo(name = "saveSum") val saveSum: BigDecimal,
    @ColumnInfo(name = "createdDate") val createdDate: Date
)
