package com.d9tilov.moneymanager.base.data.local.db.prepopulate.entity

import com.d9tilov.moneymanager.category.CategoryType

data class PrepopulateCategory(
    val id: Long,
    val parentId: Long,
    val type: CategoryType,
    val name: String,
    val icon: Int,
    val color: Int,
    val priority: Int,
    val ordinal: Int
)
