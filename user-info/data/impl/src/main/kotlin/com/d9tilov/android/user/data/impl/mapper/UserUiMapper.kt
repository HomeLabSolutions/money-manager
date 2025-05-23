package com.d9tilov.android.user.data.impl.mapper

import com.d9tilov.android.core.constants.DataConstants.DEFAULT_DATA_ID
import com.d9tilov.android.user.domain.model.UserProfile
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser?.toDataModel(): UserProfile {
    this?.run {
        val tmpArrayName = displayName?.split(" ") ?: listOf("", "")
        var parsedFirstName = ""
        var parsedSecondName = ""
        if (tmpArrayName.isNotEmpty()) {
            parsedFirstName = tmpArrayName[0]
            if (tmpArrayName.size > 1) parsedSecondName = tmpArrayName[1]
        }
        return UserProfile.EMPTY.copy(
            displayedName = displayName,
            firstName = parsedFirstName,
            lastName = parsedSecondName,
            photoUrl = photoUrl.toString(),
        )
    } ?: return UserProfile.EMPTY.copy(
        uid = DEFAULT_DATA_ID.toString(),
        displayedName = "Name Surname",
        firstName = "Name",
        lastName = "Surname",
    )
}
