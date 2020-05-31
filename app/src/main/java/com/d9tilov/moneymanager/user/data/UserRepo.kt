package com.d9tilov.moneymanager.user.data

import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.local.IUserLocalSource
import com.d9tilov.moneymanager.user.domain.IUserRepo
import io.reactivex.Completable
import io.reactivex.Flowable

class UserRepo(
    private val preferencesStore: PreferencesStore,
    private val userLocalSource: IUserLocalSource
) : IUserRepo {

    override fun getUser(): Flowable<UserProfile> = userLocalSource.getCurrentUser()
    override fun createUser(entity: UserProfile): Completable {
        preferencesStore.uid = entity.uid
        return userLocalSource.createCurrentUser(entity)
    }

    override fun updateUser(entity: UserProfile): Completable {
//        preferencesStore.budgetCreated = entity.budgetDayCreation != 0L
        return userLocalSource.updateCurrentUser(entity)
    }

    override fun logout(): Completable {
        return userLocalSource.deleteUser()
            .doOnComplete { preferencesStore.clearAllData() }
    }
}
