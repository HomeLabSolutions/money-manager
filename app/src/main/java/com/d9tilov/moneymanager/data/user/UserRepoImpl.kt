package com.d9tilov.moneymanager.data.user

import com.d9tilov.moneymanager.data.base.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.data.user.entities.UserProfile
import com.d9tilov.moneymanager.data.user.local.UserLocalSource
import com.d9tilov.moneymanager.domain.user.UserRepo
import io.reactivex.Completable
import io.reactivex.Flowable

class UserRepoImpl(
    private val preferencesStore: PreferencesStore,
    private val userLocalSource: UserLocalSource
) : UserRepo {

    override fun getUser(): Flowable<UserProfile> = userLocalSource.getCurrentUser()
    override fun createUser(entity: UserProfile): Completable {
        preferencesStore.clientId = entity.clientId
        return userLocalSource.createCurrentUser(entity)
    }

    override fun updateUser(entity: UserProfile): Completable {
//        preferencesStore.budgetCreated = entity.budgetDayCreation != 0L
        return userLocalSource.updateCurrentUser(entity)
    }

    override fun logout(entity: UserProfile): Completable {
        throw NoSuchMethodException("Empty impl")
    }
}
