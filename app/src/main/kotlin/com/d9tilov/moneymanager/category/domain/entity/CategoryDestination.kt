package com.d9tilov.moneymanager.category.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class CategoryDestination : Parcelable {
    @Parcelize
    object MainScreen : CategoryDestination()
    @Parcelize
    object MainWithSumScreen : CategoryDestination()
    @Parcelize
    object EditTransactionScreen : CategoryDestination()
    @Parcelize
    object EditRegularTransactionScreen : CategoryDestination()
    @Parcelize
    object CategoryCreationScreen : CategoryDestination()
    @Parcelize
    object CategoryScreen : CategoryDestination()
    @Parcelize
    object SubCategoryScreen : CategoryDestination()
}
