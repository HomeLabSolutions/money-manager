package com.d9tilov.moneymanager.regular.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class RegularTransactionDestination(open val name: String) : Parcelable {

    companion object {
        const val FROM_PREPOPULATE_SCREEN = "prepopulate"
        const val FROM_PROFILE_SCREEN = "profile"
    }

    @Parcelize
    object PREPOPULATE_SCREEN : RegularTransactionDestination(FROM_PREPOPULATE_SCREEN)

    @Parcelize
    object PROFILE_SCREEN : RegularTransactionDestination(FROM_PROFILE_SCREEN)
}
