package com.d9tilov.moneymanager.accumulations.data.local.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.d9tilov.moneymanager.user.data.local.entity.UserDbModel
import java.math.BigDecimal
import java.util.Date

@Entity(
    tableName = "accumulation",
    foreignKeys = [
        ForeignKey(
            entity = UserDbModel::class,
            parentColumns = ["uid"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AccumulationDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "clientId", index = true) val clientId: String,
    @ColumnInfo(name = "sum") val sum: BigDecimal,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "date") val date: Date
)
