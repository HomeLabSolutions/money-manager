package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepo {
    fun getUser(): Flow<UserProfile>
    suspend fun getCurrency(): String
    fun getBackupData(): Flow<BackupData>
    suspend fun showPrepopulate(): Boolean
    suspend fun createUser(entity: UserProfile): UserProfile
    suspend fun updateUser(entity: UserProfile)
    suspend fun backup()
}
