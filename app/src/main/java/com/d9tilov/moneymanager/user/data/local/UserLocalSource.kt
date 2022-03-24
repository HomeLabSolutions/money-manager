package com.d9tilov.moneymanager.user.data.local

import android.content.Context
import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.backup.BackupManager
import com.d9tilov.moneymanager.base.data.Result
import com.d9tilov.moneymanager.base.data.Status
import com.d9tilov.moneymanager.base.data.local.exceptions.NetworkException
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.core.util.currentDateTime
import com.d9tilov.moneymanager.core.util.isNetworkConnected
import com.d9tilov.moneymanager.core.util.toMillis
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.local.mapper.toDataModel
import com.d9tilov.moneymanager.user.data.local.mapper.toDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserLocalSource(
    private val context: Context,
    private val preferencesStore: PreferencesStore,
    private val userDao: UserDao,
    private val backupManager: BackupManager
) : UserSource {

    override suspend fun createUserOrRestore(userProfile: UserProfile): UserProfile {
        preferencesStore.uid = userProfile.uid
        val result: Result<Nothing> = backupManager.restoreDb()
        if (result.status == Status.ERROR) {
            userDao.insert(userProfile.toDbModel())
        }
        return userDao.getById(userProfile.uid).first().toDataModel()
    }

    override suspend fun updateCurrentUser(userProfile: UserProfile) {
        userDao.update(userProfile.toDbModel().copy())
    }

    override suspend fun showPrepopulate(): Boolean {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            userDao.showPrepopulate(currentUserId)
        }
    }

    override suspend fun getFiscalDay(): Int {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            userDao.getFiscalDay(currentUserId)
        }
    }

    override fun getBackupData(): Flow<BackupData> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            userDao.getBackupData(currentUserId)
        }
    }

    override fun getCurrentUser(): Flow<UserProfile> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            userDao.getById(currentUserId).map { it.toDataModel() }
        }
    }

    override suspend fun backupUser(): Result<Nothing> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Result.error(WrongUidException())
        } else {
            val user = userDao.getById(currentUserId).first()
            if (isNetworkConnected(context)) {
                userDao.update(user.copy(backupData = BackupData(currentDateTime().toMillis())))
                backupManager.backupDb()
            } else {
                Result.error(NetworkException())
            }
        }
    }

    override suspend fun deleteUser() {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            userDao.delete(currentUserId)
            preferencesStore.clearAllData()
        }
    }
}
