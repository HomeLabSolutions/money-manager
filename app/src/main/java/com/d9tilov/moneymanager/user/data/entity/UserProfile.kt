package com.d9tilov.moneymanager.user.data.entity

import com.d9tilov.moneymanager.core.constants.DataConstants

data class UserProfile(
    val uid: String,
    val displayedName: String?,
    val firstName: String?,
    val lastName: String?,
    val currentCurrencyCode: String,
    val fiscalDay: Int
) {
    companion object {
        val EMPTY = UserProfile(
            uid = DataConstants.DEFAULT_DATA_ID.toString(),
            displayedName = "",
            firstName = "",
            lastName = "",
            currentCurrencyCode = DataConstants.DEFAULT_CURRENCY_CODE,
            fiscalDay = 1
        )
    }
}
