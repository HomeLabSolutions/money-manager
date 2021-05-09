package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.domain.mapper.UserDomainMapper
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class UserInfoInteractor(
    private val userRepo: UserRepo,
    private val userDomainMapper: UserDomainMapper,
) : UserInteractor {

    override fun getCurrentUser(): Flow<UserProfile> = userRepo.getUser()
    override suspend fun getCurrency(): String = userRepo.getCurrency()
    override fun getBackupData(): Flow<BackupData> = userRepo.getBackupData()

    override suspend fun showPrepopulate(): Boolean = userRepo.showPrepopulate()

    override suspend fun createUser(user: FirebaseUser?): UserProfile =
        userRepo.createUser(userDomainMapper.toDataModel(user))

    override suspend fun updateUser(userProfile: UserProfile) {
        userRepo.updateUser(userProfile)
    }

    override suspend fun backup() {
        userRepo.backup()
    }
}
