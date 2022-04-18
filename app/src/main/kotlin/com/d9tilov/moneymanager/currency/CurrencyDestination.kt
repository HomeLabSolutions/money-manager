package com.d9tilov.moneymanager.currency

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class CurrencyDestination(open val name: String) : Parcelable {

    companion object {
        private const val FROM_PREPOPULATE_SCREEN = "prepopulate"
        private const val FROM_PROFILE_SCREEN_CURRENT = "profile_current"
        private const val FROM_EDIT_TRANSACTION_SCREEN = "edit_transaction"
        private const val FROM_EDIT_REGULAR_TRANSACTION_SCREEN = "edit_regular_transaction"
        private const val FROM_INCOME_EXPENSE_SCREEN = "income_expense"
    }

    @Parcelize
    object PREPOPULATE_SCREEN : CurrencyDestination(FROM_PREPOPULATE_SCREEN)

    @Parcelize
    object PROFILE_SCREEN_CURRENT : CurrencyDestination(FROM_PROFILE_SCREEN_CURRENT)

    @Parcelize
    object EDIT_TRANSACTION_SCREEN : CurrencyDestination(FROM_EDIT_TRANSACTION_SCREEN)

    @Parcelize
    object EDIT_REGULAR_TRANSACTION_SCREEN : CurrencyDestination(FROM_EDIT_REGULAR_TRANSACTION_SCREEN)

    @Parcelize
    object INCOME_EXPENSE_SCREEN : CurrencyDestination(FROM_INCOME_EXPENSE_SCREEN)
}
