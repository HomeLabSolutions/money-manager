package com.d9tilov.android.category.data.model

import com.d9tilov.android.core.constants.DataConstants.DEFAULT_DATA_ID
import com.d9tilov.android.core.constants.DataConstants.NO_ID
import com.d9tilov.android.core.model.TransactionType

data class Category(
    val id: Long,
    val clientId: String,
    val parent: Category?,
    val children: List<Category>,
    val type: TransactionType,
    val name: String,
    val icon: Int?,
    val color: Int?,
    val usageCount: Int
) {
    companion object {
        const val ALL_ITEMS_ID = -555L
        val EMPTY_INCOME = Category(
            id = DEFAULT_DATA_ID,
            clientId = NO_ID.toString(),
            parent = null,
            children = emptyList(),
            type = TransactionType.INCOME,
            name = "",
            icon = null,
            color = null,
            usageCount = 0
        )
        val EMPTY_EXPENSE = Category(
            id = DEFAULT_DATA_ID,
            clientId = NO_ID.toString(),
            parent = null,
            children = emptyList(),
            type = TransactionType.EXPENSE,
            name = "",
            icon = null,
            color = null,
            usageCount = 0
        )
    }
}
fun Category.isEmpty() = this == Category.EMPTY_EXPENSE || this == Category.EMPTY_INCOME
