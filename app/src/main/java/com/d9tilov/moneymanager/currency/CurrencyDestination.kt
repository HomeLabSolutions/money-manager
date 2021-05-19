package com.d9tilov.moneymanager.currency

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class CurrencyDestination(open val name: String) : Parcelable {

    companion object {
        const val FROM_PREPOPULATE_SCREEN = "prepopulate"
        const val FROM_PROFILE_SCREEN = "profile"
    }

    @Parcelize
    object PREPOPULATE_SCREEN : CurrencyDestination(FROM_PREPOPULATE_SCREEN)

    @Parcelize
    object PROFILE_SCREEN : CurrencyDestination(FROM_PROFILE_SCREEN)
}
