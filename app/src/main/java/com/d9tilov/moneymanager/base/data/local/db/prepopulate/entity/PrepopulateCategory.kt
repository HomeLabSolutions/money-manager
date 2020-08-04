package com.d9tilov.moneymanager.base.data.local.db.prepopulate.entity

import com.d9tilov.moneymanager.transaction.TransactionType

data class PrepopulateCategory(
    val id: Long,
    val parentId: Long,
    val type: TransactionType,
    val name: String,
    val icon: Int,
    val color: Int,
    val usageCount: Int
)
