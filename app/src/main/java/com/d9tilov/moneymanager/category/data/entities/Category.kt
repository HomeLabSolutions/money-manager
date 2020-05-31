package com.d9tilov.moneymanager.category.data.entities

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase.Companion.DEFAULT_DATA_ID
import com.d9tilov.moneymanager.category.CategoryType
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    val id: Long = DEFAULT_DATA_ID,
    val parent: Category? = null,
    val children: List<Category> = emptyList(),
    val type: CategoryType,
    val name: String,
    @DrawableRes val icon: Int,
    @ColorRes val color: Int
) : Parcelable {
    companion object {
        const val ALL_ITEMS_ID = -555L
    }
}
