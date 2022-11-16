package com.d9tilov.moneymanager.user.data.local.mapper

import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.entity.UserDbModel
import com.google.firebase.auth.FirebaseAuth

fun UserDbModel.toDataModel(): UserProfile = UserProfile(
    uid = uid,
    displayedName = "$firstName $lastName",
    photoUrl = FirebaseAuth.getInstance().currentUser?.photoUrl,
    firstName = firstName,
    lastName = lastName,
    currentCurrencyCode = currentCurrencyCode,
    showPrepopulate = showPrepopulate,
    fiscalDay = fiscalDay
)

fun UserProfile.toDbModel(): UserDbModel =
    UserDbModel(
        uid = uid,
        firstName = firstName,
        lastName = lastName,
        currentCurrencyCode = currentCurrencyCode,
        showPrepopulate = showPrepopulate,
        fiscalDay = fiscalDay
    )
