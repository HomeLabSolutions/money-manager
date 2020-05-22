package com.d9tilov.moneymanager.user.data.local

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.incomeexpense.data.base.local.exceptions.WrongUidException
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.local.mappers.DataUserMapper
import io.reactivex.Completable
import io.reactivex.Flowable

class UserLocalSource(
    private val preferencesStore: PreferencesStore,
    database: AppDatabase,
    private val dataUserMapper: DataUserMapper
) : IUserLocalSource {

    private val userDao: UserDao = database.userDao()

    override fun createCurrentUser(userProfile: UserProfile) =
        userDao.insert(dataUserMapper.toDbModel(userProfile))

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
        }
    }
}
