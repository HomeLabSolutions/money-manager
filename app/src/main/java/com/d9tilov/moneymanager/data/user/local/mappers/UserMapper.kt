package com.d9tilov.moneymanager.data.user.local.mappers

import com.d9tilov.moneymanager.data.user.entities.UserProfile
import com.d9tilov.moneymanager.data.user.local.entities.UserDbModel
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun convertToDataModel(user: UserDbModel) =
        with(user) {
            UserProfile(
                uid = uid,
                displayedName = userName,
                firstName = firstName,
                secondName = lastName,
                budgetDayCreation = budgetDayCreation
            )
        }

    fun convertToDbModel(clientProfileEntity: UserProfile, id: String) =
        with(clientProfileEntity) {
            UserDbModel(
                uid = id,
                userName = displayedName,
                firstName = firstName,
                lastName = secondName,
                budgetDayCreation = budgetDayCreation
            )
        }
}
