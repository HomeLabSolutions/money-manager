package com.d9tilov.moneymanager.category

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class CategoryType(open val name: String) : Parcelable {

    companion object {
        const val INCOME_CATEGORY_NAME = "Income"
        const val EXPENSE_CATEGORY_NAME = "Expense"
    }

    @Parcelize
    object INCOME : CategoryType(INCOME_CATEGORY_NAME)

    @Parcelize
    object EXPENSE : CategoryType(EXPENSE_CATEGORY_NAME)
}
