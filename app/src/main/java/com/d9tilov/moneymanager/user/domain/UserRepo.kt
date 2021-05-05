package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.user.data.entity.UserProfile
import io.reactivex.Completable
import io.reactivex.Flowable

interface UserRepo {
    fun getUser(): Flowable<UserProfile>
    fun createUser(entity: UserProfile): Completable
    fun updateUser(entity: UserProfile): Completable
    fun backup(): Completable
    fun logout(): Completable
}
