package com.d9tilov.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.d9tilov.android.core.model.Decimal
import kotlinx.datetime.LocalDateTime

@Entity(
    tableName = "goal",
    foreignKeys = [
        ForeignKey(
            entity = UserDbModel::class,
            parentColumns = ["uid"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class GoalDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "clientId", index = true) val clientId: String,
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "targetSum") val targetSum: Decimal,
    @ColumnInfo(name = "createdDate") val createdDate: LocalDateTime,
    @ColumnInfo(name = "description") val description: String,
)
