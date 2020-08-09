package com.d9tilov.moneymanager.category.data.entities

import android.os.Parcelable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryIcon(@DrawableRes val drawableId: Int, @ColorRes val colorId: Int) : Parcelable
