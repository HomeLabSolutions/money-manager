package com.d9tilov.moneymanager.user.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.local.mapper.DataUserMapper
import io.reactivex.Completable
import io.reactivex.Flowable

class UserLocalSource(
    private val preferencesStore: PreferencesStore,
    database: AppDatabase,
    private val dataUserMapper: DataUserMapper
) : UserSource {

    private val userDao: UserDao = database.userDao()

    override fun createCurrentUser(userProfile: UserProfile): Completable {
        preferencesStore.uid = userProfile.uid
        return userDao.insert(dataUserMapper.toDbModel(userProfile))
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
