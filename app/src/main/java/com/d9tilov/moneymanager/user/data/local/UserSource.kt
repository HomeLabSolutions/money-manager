package com.d9tilov.moneymanager.user.data.local

import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.base.data.Result
import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserSource : Source {

    suspend fun createUserOrRestore(userProfile: UserProfile): UserProfile
    suspend fun updateCurrentUser(userProfile: UserProfile)
    suspend fun showPrepopulate(): Boolean
    suspend fun getFiscalDay(): Int
    fun getBackupData(): Flow<BackupData>
    fun getCurrentUser(): Flow<UserProfile>
    suspend fun getCurrentCurrency(): String
    suspend fun backupUser(): Result<Nothing>
    suspend fun deleteUser()
}
