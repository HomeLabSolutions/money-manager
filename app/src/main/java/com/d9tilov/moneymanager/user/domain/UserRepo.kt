package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.base.data.Result
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepo {
    fun getUser(): Flow<UserProfile>
    fun getBackupData(): Flow<BackupData>
    suspend fun getCurrentCurrency(): String
    suspend fun showPrepopulate(): Boolean
    suspend fun getFiscalDay(): Int
    suspend fun createUser(entity: UserProfile): UserProfile
    suspend fun updateUser(entity: UserProfile)
    suspend fun backup(): Result<Nothing>
    suspend fun deleteUser()
}
