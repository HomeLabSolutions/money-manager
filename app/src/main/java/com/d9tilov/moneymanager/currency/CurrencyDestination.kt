package com.d9tilov.moneymanager.currency

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class CurrencyDestination(open val name: String) : Parcelable {

    companion object {
        private const val FROM_PREPOPULATE_SCREEN = "prepopulate"
        private const val FROM_PROFILE_SCREEN_CURRENT = "profile_current"
    }

    @Parcelize
    object PREPOPULATE_SCREEN : CurrencyDestination(FROM_PREPOPULATE_SCREEN)

    @Parcelize
    object PROFILE_SCREEN_CURRENT : CurrencyDestination(FROM_PROFILE_SCREEN_CURRENT)
}
