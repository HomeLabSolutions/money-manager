package com.d9tilov.moneymanager.domain.user

import com.d9tilov.moneymanager.data.user.entities.UserProfile
import io.reactivex.Completable
import io.reactivex.Flowable

interface UserRepo {
    fun getUser(): Flowable<UserProfile>
    fun createUser(entity: UserProfile): Completable
    fun updateUser(entity: UserProfile): Completable
    fun logout(): Completable
}
