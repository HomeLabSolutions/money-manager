package com.d9tilov.moneymanager.data.user.local.mappers

import com.d9tilov.moneymanager.data.user.entities.UserProfile
import com.d9tilov.moneymanager.data.user.local.entities.UserDbModel
import javax.inject.Inject

class UserMapper @Inject constructor() {

    fun convertToDataModel(user: UserDbModel) =
        with(user) {
            UserProfile(
                clientId = clientId,
                displayedName = userName,
                firstName = firstName,
                secondName = lastName,
                pushNotificationSound = pushSound,
                pushReportEnabled = pushReportEnabled,
                hideTransactionDetailsInPushMessages = hideTransactionDetails,
                fiscalDay = fiscalDay,
                budgetDayCreation = budgetDayCreation
            )
        }

    fun convertToDbModel(clientProfileEntity: UserProfile, id: String) =
        with(clientProfileEntity) {
            UserDbModel(
                clientId = id,
                userName = displayedName,
                firstName = firstName,
                lastName = secondName,
                pushSound = pushNotificationSound,
                pushReportEnabled = pushReportEnabled,
                hideTransactionDetails = hideTransactionDetailsInPushMessages,
                fiscalDay = fiscalDay,
                budgetDayCreation = budgetDayCreation
            )
        }
}
