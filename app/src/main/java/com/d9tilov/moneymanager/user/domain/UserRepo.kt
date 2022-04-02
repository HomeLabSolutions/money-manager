package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.base.data.Result
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepo {
    fun getCurrentUser(): Flow<UserProfile>
    fun getBackupData(): Flow<BackupData>
    suspend fun showPrepopulate(): Boolean
    suspend fun getFiscalDay(): Int
    suspend fun create(entity: UserProfile): UserProfile
    suspend fun update(entity: UserProfile)
    suspend fun backup(): Result<BackupData>
    suspend fun delete()
}
