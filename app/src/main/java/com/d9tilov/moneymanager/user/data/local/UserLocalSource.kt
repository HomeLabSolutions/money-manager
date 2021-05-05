package com.d9tilov.moneymanager.user.data.local

import android.accounts.NetworkErrorException
import android.content.Context
import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.backup.BackupManager
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.core.util.isNetworkConnected
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.local.mapper.DataUserMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.Calendar

class UserLocalSource(
    private val context: Context,
    private val preferencesStore: PreferencesStore,
    database: AppDatabase,
    private val dataUserMapper: DataUserMapper,
    private val backupManager: BackupManager
) : UserSource {

    private val userDao: UserDao = database.userDao()

    override fun createUserOrRestore(userProfile: UserProfile): Completable {
        preferencesStore.uid = userProfile.uid
        return backupManager.restoreDb(userProfile.uid)
            .onErrorResumeNext { userDao.insert(dataUserMapper.toDbModel(userProfile)) }
    }

    override fun updateCurrentUser(userProfile: UserProfile) =
        userDao.update(dataUserMapper.toDbModel(userProfile))

    override fun getCurrentUser(): Flowable<UserProfile> {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Flowable.error(WrongUidException())
        } else {
            return userDao.getById(currentUserId).map { dataUserMapper.toDataModel(it) }
        }
    }

    override fun backupUser(): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            userDao.getById(currentUserId)
                .firstOrError()
                .flatMap {
                    if (isNetworkConnected(context)) {
                        return@flatMap Single.fromCallable { it }
                    } else {
                        return@flatMap Single.error(NetworkErrorException())
                    }
                }
                .flatMapCompletable {
                    userDao.update(
                        it.copy(
                            backupData = BackupData(
                                Calendar.getInstance().timeInMillis
                            )
                        )
                    ).andThen(backupManager.backupDb(currentUserId))
                }
        }
    }

    override fun deleteUser(): Completable {
        val currentUserId = preferencesStore.uid
        return if (currentUserId == null) {
            Completable.error(WrongUidException())
        } else {
            return userDao.delete(currentUserId)
                .doOnComplete { preferencesStore.clearAllData() }
        }
    }
}
