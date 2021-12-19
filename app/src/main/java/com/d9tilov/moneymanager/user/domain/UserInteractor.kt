package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.base.data.Result
import com.d9tilov.moneymanager.base.domain.Interactor
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserInteractor : Interactor {

    fun getCurrentUser(): Flow<UserProfile>
    fun getBackupData(): Flow<BackupData>
    suspend fun getCurrentCurrency(): String
    suspend fun showPrepopulate(): Boolean
    suspend fun getFiscalDay(): Int
    suspend fun createUser(user: FirebaseUser?): UserProfile
    suspend fun updateUser(userProfile: UserProfile)
    suspend fun backup(): Result<Nothing>
    suspend fun deleteUser()
}
