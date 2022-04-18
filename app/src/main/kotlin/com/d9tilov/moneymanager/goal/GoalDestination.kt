package com.d9tilov.moneymanager.goal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class GoalDestination(open val name: String) : Parcelable {

    companion object {
        const val FROM_PREPOPULATE_SCREEN = "prepopulate"
        const val FROM_PROFILE_SCREEN = "profile"
    }

    @Parcelize
    object PREPOPULATE_SCREEN : GoalDestination(FROM_PREPOPULATE_SCREEN)

    @Parcelize
    object PROFILE_SCREEN : GoalDestination(FROM_PROFILE_SCREEN)
}
