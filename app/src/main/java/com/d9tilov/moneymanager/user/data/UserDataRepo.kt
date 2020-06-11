package com.d9tilov.moneymanager.user.data

import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.data.local.UserSource
import com.d9tilov.moneymanager.user.domain.UserRepo
import io.reactivex.Completable
import io.reactivex.Flowable

class UserDataRepo(
    private val userLocalSource: UserSource
) : UserRepo {

    override fun getUser(): Flowable<UserProfile> = userLocalSource.getCurrentUser()
    override fun createUser(entity: UserProfile): Completable {
        return userLocalSource.createCurrentUser(entity)
    }

    override fun updateUser(entity: UserProfile): Completable {
//        preferencesStore.budgetCreated = entity.budgetDayCreation != 0L
        return userLocalSource.updateCurrentUser(entity)
    }

    override fun logout(): Completable {
        return userLocalSource.deleteUser()
    }
}
