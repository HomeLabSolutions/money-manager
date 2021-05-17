package com.d9tilov.moneymanager.user.data.local

import android.content.Context
import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.backup.BackupManager
import com.d9tilov.moneymanager.base.data.Result
import com.d9tilov.moneymanager.base.data.Status
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.NetworkException
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.core.util.isNetworkConnected
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.local.mapper.DataUserMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Date

class UserLocalSource(
    private val context: Context,
    private val preferencesStore: PreferencesStore,
    database: AppDatabase,
    private val dataUserMapper: DataUserMapper,
    private val backupManager: BackupManager
) : UserSource {

    private val userDao: UserDao = database.userDao()

    override suspend fun createUserOrRestore(userProfile: UserProfile): UserProfile {
        preferencesStore.uid = userProfile.uid
        val result: Result<Nothing> = backupManager.restoreDb(userProfile.uid)
        if (result.status == Status.ERROR) {
            userDao.insert(dataUserMapper.toDbModel(userProfile))
        }
        return dataUserMapper.toDataModel(userDao.getById(userProfile.uid).first())
    }

    override suspend fun updateCurrentUser(userProfile: UserProfile) {
        userDao.update(dataUserMapper.toDbModel(userProfile).copy())
    }

    override suspend fun showPrepopulate(): Boolean {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            userDao.showPrepopulate(currentUserId)
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
            userDao.getById(currentUserId).map { dataUserMapper.toDataModel(it) }
        }
    }

    override suspend fun getCurrency(): String {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            throw WrongUidException()
        } else {
            userDao.getCurrency(currentUserId)
        }
    }

    override suspend fun backupUser() {
        val currentUserId = preferencesStore.uid
        if (currentUserId == null) {
            throw WrongUidException()
        } else {
            val user = userDao.getById(currentUserId).first()
            if (isNetworkConnected(context)) {
                userDao.update(user.copy(backupData = BackupData(Date().time)))
                backupManager.backupDb(currentUserId)
            } else {
                throw NetworkException()
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
