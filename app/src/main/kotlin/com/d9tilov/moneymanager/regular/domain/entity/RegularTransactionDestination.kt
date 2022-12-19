package com.d9tilov.moneymanager.regular.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class RegularTransactionDestination : Parcelable {

    @Parcelize
    object PrepopulateScreen : RegularTransactionDestination()

    @Parcelize
    object ProfileScreen : RegularTransactionDestination()
}
