package com.d9tilov.moneymanager.budget.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class BudgetDestination : Parcelable {

    @Parcelize
    object PrepopulateScreen : BudgetDestination()

    @Parcelize
    object ProfileScreen : BudgetDestination()
}
