package com.d9tilov.moneymanager.user.data.local.mapper

import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.local.entity.UserDbModel

fun UserDbModel.toDataModel(): UserProfile = UserProfile(
    uid = uid,
    displayedName = "$firstName $lastName",
    firstName = firstName,
    lastName = lastName,
    mainCurrencyCode = mainCurrencyCode,
    currentCurrencyCode = currentCurrencyCode,
    showPrepopulate = showPrepopulate,
    backupData = backupData,
    fiscalDay = fiscalDay
)

fun UserProfile.toDbModel(): UserDbModel =
    UserDbModel(
        uid = uid,
        firstName = firstName,
        lastName = lastName,
        mainCurrencyCode = mainCurrencyCode,
        currentCurrencyCode = currentCurrencyCode,
        showPrepopulate = showPrepopulate,
        backupData = backupData,
        fiscalDay = fiscalDay
    )
