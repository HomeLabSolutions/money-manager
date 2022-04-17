package com.d9tilov.moneymanager.user.data

import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.local.UserSource
import com.d9tilov.moneymanager.user.domain.UserRepo
import kotlinx.coroutines.flow.Flow

class UserDataRepo(private val userLocalSource: UserSource) : UserRepo {

    override fun getCurrentUser(): Flow<UserProfile?> = userLocalSource.getCurrentUser()

    override suspend fun getFiscalDay(): Int = userLocalSource.getFiscalDay()

    override suspend fun create(entity: UserProfile): UserProfile =
        userLocalSource.createUserOrRestore(entity)

    override suspend fun update(entity: UserProfile) {
        userLocalSource.updateCurrentUser(entity)
    }

    override suspend fun delete() {
        userLocalSource.deleteUser()
    }
}
