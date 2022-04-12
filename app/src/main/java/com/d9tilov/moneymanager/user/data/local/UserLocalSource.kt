package com.d9tilov.moneymanager.user.data.local

import android.content.Context
import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.backup.BackupManager
import com.d9tilov.moneymanager.base.data.ResultOf
import com.d9tilov.moneymanager.base.data.local.exceptions.NetworkException
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.core.util.isNetworkConnected
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.local.mapper.toDataModel
import com.d9tilov.moneymanager.user.data.local.mapper.toDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class UserLocalSource(
    private val context: Context,
    private val preferencesStore: PreferencesStore,
    private val userDao: UserDao,
    private val backupManager: BackupManager
) : UserSource {

    override suspend fun createUserOrRestore(userProfile: UserProfile): UserProfile {
        preferencesStore.updateUid(userProfile.uid)
        val result: ResultOf<Any> = backupManager.restoreDb()
        if (result is ResultOf.Failure) userDao.insert(userProfile.toDbModel())
        val dbUser = userDao.getById(userProfile.uid).first().toDataModel()
        val currencyCode = dbUser.currentCurrencyCode
        preferencesStore.updateCurrentCurrency(currencyCode)
        return dbUser
    }

    override suspend fun updateCurrentUser(userProfile: UserProfile) {
        userDao.update(userProfile.toDbModel().copy())
    }

    override suspend fun showPrepopulate(): Boolean {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return if (currentUserId == null) throw WrongUidException()
        else userDao.showPrepopulate(currentUserId)
    }

    override suspend fun getFiscalDay(): Int {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        return if (currentUserId == null) throw WrongUidException()
        else userDao.getFiscalDay(currentUserId)
    }

    override fun getBackupData(): Flow<BackupData> {
        return preferencesStore.uid.flatMapMerge { uid ->
            if (uid == null) throw WrongUidException()
            else userDao.getBackupData(uid)
        }
    }

    override fun getCurrentUser(): Flow<UserProfile> {
        return preferencesStore.uid.flatMapMerge { uid ->
            if (uid == null) throw WrongUidException()
            else userDao.getById(uid).map { it.toDataModel() }
        }
    }

    override suspend fun backupUser(): ResultOf<BackupData> {
        val currentUserId = preferencesStore.uid.first()
        return if (currentUserId == null) ResultOf.Failure(WrongUidException())
        else {
            if (isNetworkConnected(context)) {
                val backupData: ResultOf<BackupData> = backupManager.backupDb()
                if (backupData is ResultOf.Success) {
                    val user = getCurrentUser().first()
                    updateCurrentUser(user.copy(backupData = backupData.data))
                }
                backupData
            } else ResultOf.Failure(NetworkException())
        }
    }

    override suspend fun deleteUser() {
        val currentUserId = withContext(Dispatchers.IO) { preferencesStore.uid.first() }
        if (currentUserId == null) throw WrongUidException()
        else preferencesStore.clearAllData()
    }
}
