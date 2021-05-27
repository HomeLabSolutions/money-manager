package com.d9tilov.moneymanager.user.data

import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.base.data.Result
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.local.UserSource
import com.d9tilov.moneymanager.user.domain.UserRepo
import kotlinx.coroutines.flow.Flow

class UserDataRepo(
    private val userLocalSource: UserSource
) : UserRepo {

    override fun getUser(): Flow<UserProfile> = userLocalSource.getCurrentUser()
    override suspend fun getCurrency(): String = userLocalSource.getCurrency()
    override fun getBackupData(): Flow<BackupData> = userLocalSource.getBackupData()

    override suspend fun showPrepopulate(): Boolean = userLocalSource.showPrepopulate()
    override suspend fun getFiscalDay(): Int = userLocalSource.getFiscalDay()

    override suspend fun createUser(entity: UserProfile): UserProfile =
        userLocalSource.createUserOrRestore(entity)

    override suspend fun updateUser(entity: UserProfile) {
        userLocalSource.updateCurrentUser(entity)
    }

    override suspend fun backup(): Result<Nothing> = userLocalSource.backupUser()

    override suspend fun deleteUser() {
        userLocalSource.deleteUser()
    }
}
