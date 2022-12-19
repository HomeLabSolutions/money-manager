package com.d9tilov.android.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.d9tilov.android.database.model.TransactionType

@Entity(
    tableName = "categories",
    foreignKeys = [
        ForeignKey(
            entity = UserDbModel::class,
            parentColumns = ["uid"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CategoryDbModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "clientId", index = true) val uid: String,
    @ColumnInfo(name = "parentId") val parentId: Long,
    @ColumnInfo(name = "type") val type: TransactionType,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "icon") val iconNameOrdinal: Int,
    @ColumnInfo(name = "color") val colorNameOrdinal: Int,
    @ColumnInfo(name = "usageCount") val usageCount: Int
)
