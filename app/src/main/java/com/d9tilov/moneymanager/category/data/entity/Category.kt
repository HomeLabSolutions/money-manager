package com.d9tilov.moneymanager.category.data.entity

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_DATA_ID
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: Long,
    val clientId: String,
    val parent: Category?,
    val children: List<Category>,
    val type: TransactionType,
    val name: String,
    @DrawableRes val icon: Int,
    @ColorRes val color: Int,
    val usageCount: Int
) : Parcelable {
    companion object {
        const val ALL_ITEMS_ID = -555L
        val EMPTY = Category(
            id = DEFAULT_DATA_ID,
            clientId = NO_ID.toString(),
            parent = null,
            children = emptyList(),
            type = TransactionType.EXPENSE,
            name = "",
            icon = R.drawable.ic_category_backery,
            color = R.color.primary_color,
            usageCount = 0
        )
    }
}
