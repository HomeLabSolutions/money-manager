package com.d9tilov.moneymanager.regular.ui.notification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class ActionType(open val name: String) : Parcelable {
    companion object {
        const val NAME_ACTION_BUTTON = "button"
        const val NAME_ACTION_DEEP_LINK = "deeplink"
    }

    @Parcelize
    object BUTTON : ActionType(NAME_ACTION_BUTTON)

    @Parcelize
    object DEEP_LINK : ActionType(NAME_ACTION_DEEP_LINK)
}
