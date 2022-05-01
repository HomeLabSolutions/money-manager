package com.d9tilov.moneymanager.budget.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class BudgetDestination(open val name: String) : Parcelable {

    companion object {
        const val FROM_PREPOPULATE_SCREEN = "prepopulate"
        const val FROM_PROFILE_SCREEN = "profile"
    }

    @Parcelize
    object PREPOPULATE_SCREEN : BudgetDestination(FROM_PREPOPULATE_SCREEN)

    @Parcelize
    object PROFILE_SCREEN : BudgetDestination(FROM_PROFILE_SCREEN)
}
