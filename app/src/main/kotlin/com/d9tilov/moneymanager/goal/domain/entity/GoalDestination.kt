package com.d9tilov.moneymanager.goal.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class GoalDestination : Parcelable {

    @Parcelize
    object PrepopulateScreen : GoalDestination()

    @Parcelize
    object ProfileScreen : GoalDestination()
}
