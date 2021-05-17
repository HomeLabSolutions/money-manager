package com.d9tilov.moneymanager.user.data.local.mapper

import com.d9tilov.moneymanager.core.mapper.Mapper
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.local.entity.UserDbModel
import javax.inject.Inject

class DataUserMapper @Inject constructor() : Mapper<UserDbModel, UserProfile> {

    override fun toDataModel(model: UserDbModel) =
        with(model) {
            UserProfile(
                uid = uid,
                displayedName = "$firstName $lastName",
                firstName = firstName,
                lastName = lastName,
                currencyCode = currencyCode,
                showPrepopulate = showPrepopulate,
                backupData = backupData
            )
        }

    override fun toDbModel(model: UserProfile) =
        with(model) {
            UserDbModel(
                uid = uid,
                firstName = firstName,
                lastName = lastName,
                currencyCode = currencyCode,
                showPrepopulate = showPrepopulate,
                backupData = backupData
            )
        }
}
