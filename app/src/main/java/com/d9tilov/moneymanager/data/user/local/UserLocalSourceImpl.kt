package com.d9tilov.moneymanager.data.user.local

import com.d9tilov.moneymanager.data.base.local.db.AppDatabase
import com.d9tilov.moneymanager.data.base.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.data.user.entities.UserProfile
import com.d9tilov.moneymanager.data.user.local.mappers.UserMapper
import com.d9tilov.moneymanager.incomeexpense.data.base.local.exceptions.WrongClientIdException
import io.reactivex.Completable
import io.reactivex.Flowable

class UserLocalSourceImpl(
    private val preferencesStore: PreferencesStore,
    database: AppDatabase,
    private val userMapper: UserMapper
) : UserLocalSource {

    private val userDao: UserDao = database.userDao()

    override fun createCurrentUser(userProfile: UserProfile): Completable {
        val currentUserId = preferencesStore.clientId
        return if (currentUserId == null) {
            Completable.error(WrongClientIdException())
        } else {
            userDao.insert(userMapper.convertToDbModel(userProfile, currentUserId))
        }
    }

    override fun updateCurrentUser(userProfile: UserProfile): Completable {
        val currentUserId = preferencesStore.clientId
        return if (currentUserId == null) {
            Completable.error(WrongClientIdException())
        } else {
            return userDao.update(userMapper.convertToDbModel(userProfile, currentUserId))
        }
    }

    override fun getCurrentUser(): Flowable<UserProfile> {
        val currentUserId = preferencesStore.clientId
        return if (currentUserId == null) {
            Flowable.error(WrongClientIdException())
        } else {
            return userDao.getById(currentUserId).map { userMapper.convertToDataModel(it) }
        }
    }

    override fun deleteUser(userProfile: UserProfile): Completable {
        val currentUserId = preferencesStore.clientId
        return if (currentUserId == null) {
            Completable.error(WrongClientIdException())
        } else {
            return userDao.deleteUser(userMapper.convertToDbModel(userProfile, currentUserId))
        }
    }
}