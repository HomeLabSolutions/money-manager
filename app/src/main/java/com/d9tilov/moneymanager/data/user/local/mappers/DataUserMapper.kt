package com.d9tilov.moneymanager.data.user.local.mappers

import com.d9tilov.moneymanager.data.user.entities.UserProfile
import com.d9tilov.moneymanager.data.user.local.entities.UserDbModel
import javax.inject.Inject

class DataUserMapper @Inject constructor() {

    fun toDataModel(user: UserDbModel) =
        with(user) {
            UserProfile(
                uid = uid,
                displayedName = "$firstName $lastName",
                firstName = firstName,
                lastName = lastName,
                budgetDayCreation = budgetDayCreation
            )
        }

    fun toDbModel(clientProfileEntity: UserProfile) =
        with(clientProfileEntity) {
            UserDbModel(
                uid = uid,
                firstName = firstName,
                lastName = lastName,
                budgetDayCreation = budgetDayCreation
            )
        }
}
