package com.d9tilov.moneymanager.base.data.local.db.prepopulate.entity

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType

data class PrepopulateCategory(
    val id: Long,
    val parentId: Long,
    val type: TransactionType,
    val name: String,
    @DrawableRes val icon: Int,
    @ColorRes val color: Int,
    val usageCount: Int
)
