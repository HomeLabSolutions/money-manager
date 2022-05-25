package com.d9tilov.moneymanager.currency.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class CurrencyDestination : Parcelable {

    @Parcelize
    object PrepopulateScreen : CurrencyDestination()

    @Parcelize
    object ProfileCurrentScreen : CurrencyDestination()

    @Parcelize
    object EditTransactionScreen : CurrencyDestination()

    @Parcelize
    object EditRegularTransactionScreen : CurrencyDestination()

    @Parcelize
    object IncomeExpenseScreen : CurrencyDestination()
}
