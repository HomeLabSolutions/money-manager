package com.d9tilov.android.user.data.impl

import com.d9tilov.android.user.data.contract.UserSource
import com.d9tilov.android.user.domain.contract.UserRepo
import com.d9tilov.android.user.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

class UserDataRepo(private val userLocalSource: UserSource) : UserRepo {

    override fun getCurrentUser(): Flow<UserProfile?> = userLocalSource.getCurrentUser()

    override suspend fun showPrepopulate(): Boolean = userLocalSource.showPrepopulate()
    override suspend fun prepopulateCompleted() = userLocalSource.prepopulateCompleted()

    override suspend fun getFiscalDay(): Int = userLocalSource.getFiscalDay()

    override suspend fun create(entity: UserProfile): UserProfile =
        userLocalSource.createUserOrRestore(entity)

    override suspend fun updateFiscalDay(fiscalDay: Int) = userLocalSource.updateFiscalDay(fiscalDay)

    override suspend fun delete() = userLocalSource.deleteUser()
}
