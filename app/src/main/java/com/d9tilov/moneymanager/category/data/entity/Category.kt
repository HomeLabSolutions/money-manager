package com.d9tilov.moneymanager.category.data.entity

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_DATA_ID
import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.NO_ID
import com.d9tilov.moneymanager.transaction.TransactionType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val id: Long = DEFAULT_DATA_ID,
    val clientId: String = NO_ID.toString(),
    val parent: Category? = null,
    val children: List<Category> = emptyList(),
    val type: TransactionType,
    val name: String,
    @DrawableRes val icon: Int,
    @ColorRes val color: Int,
    val usageCount: Int = 0
) : Parcelable {
    companion object {
        const val ALL_ITEMS_ID = -555L
    }
}
