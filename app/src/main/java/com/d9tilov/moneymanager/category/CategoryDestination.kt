package com.d9tilov.moneymanager.category

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class CategoryDestination(open val name: String) : Parcelable {

    companion object {
        const val FROM_MAIN_SCREEN = "main"
        const val FROM_EDIT_TRANSACTION_SCREEN = "edit_transaction"
    }

    @Parcelize
    object MAIN_SCREEN : CategoryDestination(FROM_MAIN_SCREEN)

    @Parcelize
    object EDIT_TRANSACTION_SCREEN : CategoryDestination(FROM_EDIT_TRANSACTION_SCREEN)
}
