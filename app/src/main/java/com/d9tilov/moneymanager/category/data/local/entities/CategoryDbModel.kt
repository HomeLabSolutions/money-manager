package com.d9tilov.moneymanager.category.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase.Companion.NO_ID

@Entity(tableName = "categories")
data class CategoryDbModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "parentId") val parentId: Long = NO_ID,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "icon", typeAffinity = ColumnInfo.BLOB) val icon: ByteArray,
    @ColumnInfo(name = "color") val color: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CategoryDbModel) return false

        if (id != other.id) return false
        if (parentId != other.parentId) return false
        if (name != other.name) return false
        if (!icon.contentEquals(other.icon)) return false
        if (color != other.color) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + parentId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + icon.contentHashCode()
        result = 31 * result + color
        return result
    }
}
