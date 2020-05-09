package com.d9tilov.moneymanager.data.base.local.db.prepopulate.entity

data class PrepopulateCategory(
    val id: Long,
    val parentId: Long,
    val name: String,
    val icon: ByteArray,
    val color: Int
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PrepopulateCategory) return false

        if (name != other.name) return false
        if (!icon.contentEquals(other.icon)) return false
        if (color != other.color) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + icon.contentHashCode()
        result = 31 * result + color
        return result
    }
}