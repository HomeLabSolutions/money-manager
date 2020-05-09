package com.d9tilov.moneymanager.data.category.entities

import com.d9tilov.moneymanager.data.base.local.db.AppDatabase.Companion.DEFAULT_DATA_ID

data class Category(
    val id: Long = DEFAULT_DATA_ID,
    val parent: Category? = null,
    val children: List<Category> = emptyList(),
    val name: String,
    val icon: ByteArray,
    val color: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Category) return false

        if (id != other.id) return false
        if (parent != other.parent) return false
        if (children != other.children) return false
        if (name != other.name) return false
        if (!icon.contentEquals(other.icon)) return false
        if (color != other.color) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (parent?.hashCode() ?: 0)
        result = 31 * result + children.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + icon.contentHashCode()
        result = 31 * result + color
        return result
    }
}