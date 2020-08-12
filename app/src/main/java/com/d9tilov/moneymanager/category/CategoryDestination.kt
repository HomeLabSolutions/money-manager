package com.d9tilov.moneymanager.category

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class CategoryDestination(open val name: String) : Parcelable {

    companion object {
        const val FROM_MAIN_SCREEN = "main"
        const val FROM_MAIN_WITH_SUM_SCREEN = "main_with_sum"
        const val FROM_EDIT_TRANSACTION_SCREEN = "edit_transaction"
        const val FROM_CATEGORY_CREATION_SCREEN = "category_creation_screen"
        const val FROM_CATEGORY_SCREEN = "category"
        const val FROM_SUB_CATEGORY_SCREEN = "sub_category"
    }

    @Parcelize
    object MAIN_SCREEN : CategoryDestination(FROM_MAIN_SCREEN)

    @Parcelize
    object MAIN_WITH_SUM_SCREEN : CategoryDestination(FROM_MAIN_WITH_SUM_SCREEN)

    @Parcelize
    object EDIT_TRANSACTION_SCREEN : CategoryDestination(FROM_EDIT_TRANSACTION_SCREEN)

    @Parcelize
    object CATEGORY_CREATION_SCREEN : CategoryDestination(FROM_CATEGORY_CREATION_SCREEN)

    @Parcelize
    object CATEGORY_SCREEN : CategoryDestination(FROM_CATEGORY_SCREEN)

    @Parcelize
    object SUB_CATEGORY_SCREEN : CategoryDestination(FROM_SUB_CATEGORY_SCREEN)
}
