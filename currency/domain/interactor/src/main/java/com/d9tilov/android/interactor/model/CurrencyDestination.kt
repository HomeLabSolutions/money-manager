package com.d9tilov.android.interactor.model

sealed class CurrencyDestination {

    object PrepopulateScreen : CurrencyDestination()

    object ProfileCurrentScreen : CurrencyDestination()

    object EditTransactionScreen : CurrencyDestination()

    object EditRegularTransactionScreen : CurrencyDestination()

    object IncomeExpenseScreen : CurrencyDestination()
}
