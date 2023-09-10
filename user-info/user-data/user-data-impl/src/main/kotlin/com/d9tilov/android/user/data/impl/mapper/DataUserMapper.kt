package com.d9tilov.android.user.data.impl.mapper

import com.d9tilov.android.database.entity.UserDbModel
import com.d9tilov.android.user.domain.model.UserProfile

fun UserDbModel.toDataModel(): UserProfile =
    UserProfile(
        uid = uid,
        displayedName = "$firstName $lastName",
        photoUrl = photoUrl,
        firstName = firstName,
        lastName = lastName,
        showPrepopulate = showPrepopulate,
        fiscalDay = fiscalDay
    )

fun UserProfile.toDbModel(): UserDbModel = UserDbModel(
    uid = uid,
    firstName = firstName,
    lastName = lastName,
    photoUrl = photoUrl,
    showPrepopulate = showPrepopulate,
    fiscalDay = fiscalDay
)
