package com.d9tilov.moneymanager.category.data.entity

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.d9tilov.moneymanager.R
import com.d9tilov.moneymanager.core.constants.DataConstants.DEFAULT_DATA_ID
import com.d9tilov.moneymanager.core.constants.DataConstants.NO_ID
import com.d9tilov.moneymanager.transaction.domain.entity.TransactionType
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
        val EMPTY_INCOME = Category(
            id = DEFAULT_DATA_ID,
            clientId = NO_ID.toString(),
            parent = null,
            children = emptyList(),
            type = TransactionType.INCOME,
            name = "",
            icon = R.drawable.ic_category_salary,
            color = R.color.category_green,
            usageCount = 0
        )
        val EMPTY_EXPENSE = Category(
            id = DEFAULT_DATA_ID,
            clientId = NO_ID.toString(),
            parent = null,
            children = emptyList(),
            type = TransactionType.EXPENSE,
            name = "",
            icon = R.drawable.ic_category_coffee2,
            color = R.color.category_pink,
            usageCount = 0
        )
    }
}
fun Category.isEmpty() = this == Category.EMPTY_EXPENSE || this == Category.EMPTY_INCOME
