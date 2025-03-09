package com.d9tilov.android.category.domain.model

import com.d9tilov.android.core.model.TransactionType

data class DefaultCategory(
    val id: Long,
    val parentId: Long,
    val type: TransactionType,
    val name: String,
    val icon: Int,
    val color: Int,
    val usageCount: Int,
)
