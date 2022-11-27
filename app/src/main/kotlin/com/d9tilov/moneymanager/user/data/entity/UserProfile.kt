package com.d9tilov.moneymanager.user.data.entity

import android.net.Uri
import com.d9tilov.moneymanager.core.constants.DataConstants

data class UserProfile(
    val uid: String,
    val displayedName: String?,
    val photoUrl: Uri?,
    val firstName: String?,
    val lastName: String?,
    val showPrepopulate: Boolean,
    val fiscalDay: Int
) {
    companion object {
        val EMPTY = UserProfile(
            uid = DataConstants.DEFAULT_DATA_ID.toString(),
            displayedName = "",
            photoUrl = null,
            firstName = "",
            lastName = "",
            showPrepopulate = true,
            fiscalDay = 1
        )
    }
}
