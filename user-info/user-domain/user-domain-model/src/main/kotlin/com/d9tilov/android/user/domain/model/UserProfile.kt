package com.d9tilov.android.user.domain.model

import com.d9tilov.android.core.constants.DataConstants

data class UserProfile(
    val uid: String,
    val displayedName: String?,
    val photoUrl: String?,
    val firstName: String?,
    val lastName: String?,
    val showPrepopulate: Boolean,
    val fiscalDay: Int,
) {
    companion object {
        val EMPTY =
            UserProfile(
                uid = DataConstants.DEFAULT_DATA_ID.toString(),
                displayedName = "",
                photoUrl = null,
                firstName = "",
                lastName = "",
                showPrepopulate = true,
                fiscalDay = 1,
            )
    }
}
