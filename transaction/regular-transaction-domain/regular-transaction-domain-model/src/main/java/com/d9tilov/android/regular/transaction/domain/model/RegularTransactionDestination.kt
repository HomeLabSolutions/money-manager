package com.d9tilov.android.regular.transaction.domain.model

sealed class RegularTransactionDestination {

    object PrepopulateScreen : RegularTransactionDestination()

    object ProfileScreen : RegularTransactionDestination()
}
