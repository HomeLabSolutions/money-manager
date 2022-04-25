package com.d9tilov.moneymanager.user.domain.mapper

import com.d9tilov.moneymanager.core.constants.DataConstants.Companion.DEFAULT_DATA_ID
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.google.firebase.auth.FirebaseUser
import java.util.Calendar
import javax.inject.Inject

class UserDomainMapper @Inject constructor() {

    fun toDataModel(user: FirebaseUser?): UserProfile {
        user?.run {
            let {
                val tmpArrayName = displayName?.split(" ") ?: listOf("", "")
                var parsedFirstName = ""
                var parsedSecondName = ""
                if (tmpArrayName.isNotEmpty()) {
                    parsedFirstName = tmpArrayName[0]
                    if (tmpArrayName.size > 1) {
                        parsedSecondName = tmpArrayName[1]
                    }
                }
                return UserProfile.EMPTY.copy(
                    displayedName = displayName,
                    firstName = parsedFirstName,
                    lastName = parsedSecondName,
                    fiscalDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                )
            }
        } ?: return UserProfile.EMPTY.copy(
            uid = DEFAULT_DATA_ID.toString(),
            displayedName = "Name Surname",
            firstName = "Name",
            lastName = "Surname",
            fiscalDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
    }
}
