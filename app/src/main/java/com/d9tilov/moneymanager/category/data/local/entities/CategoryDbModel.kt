package com.d9tilov.moneymanager.category.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.d9tilov.moneymanager.category.CategoryType
import com.d9tilov.moneymanager.user.data.local.entity.UserDbModel

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
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "clientId", index = true) val uid: String,
    @ColumnInfo(name = "parentId") val parentId: Long,
    @ColumnInfo(name = "type") val type: CategoryType,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "icon") val iconName: String,
    @ColumnInfo(name = "color") val colorName: String,
    @ColumnInfo(name = "priority") val priority: Int,
    @ColumnInfo(name = "ordinal") val ordinal: Int
)
