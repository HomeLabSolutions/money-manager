package com.d9tilov.moneymanager.category.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase.Companion.NO_ID
import com.d9tilov.moneymanager.category.CategoryType

@Entity(tableName = "categories")
data class CategoryDbModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "parentId") val parentId: Long = NO_ID,
    @ColumnInfo(name = "type") val type: CategoryType,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "icon") val icon: Int,
    @ColumnInfo(name = "color") val color: Int
)
