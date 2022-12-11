package com.d9tilov.moneymanager.regular.domain.entity

sealed class RegularTransactionDestination {

    object PrepopulateScreen : RegularTransactionDestination()

    object ProfileScreen : RegularTransactionDestination()
}
