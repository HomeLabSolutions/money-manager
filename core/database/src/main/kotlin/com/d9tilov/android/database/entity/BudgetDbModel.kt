package com.d9tilov.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.d9tilov.android.core.model.Decimal
import kotlinx.datetime.LocalDateTime

@Entity(
    tableName = "budget",
    foreignKeys = [
        ForeignKey(
            entity = UserDbModel::class,
            parentColumns = ["uid"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class BudgetDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "clientId", index = true) val clientId: String,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "sum") val sum: Decimal,
    @ColumnInfo(name = "saveSum") val saveSum: Decimal,
    @ColumnInfo(name = "createdDate") val createdDate: LocalDateTime,
)
