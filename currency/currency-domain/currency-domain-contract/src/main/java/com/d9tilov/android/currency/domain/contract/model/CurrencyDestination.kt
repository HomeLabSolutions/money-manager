package com.d9tilov.android.currency.domain.contract.model

sealed class CurrencyDestination {

    object PrepopulateScreen : CurrencyDestination()

    object ProfileCurrentScreen : CurrencyDestination()

    object EditTransactionScreen : CurrencyDestination()

    object EditRegularTransactionScreen : CurrencyDestination()

    object IncomeExpenseScreen : CurrencyDestination()
}
