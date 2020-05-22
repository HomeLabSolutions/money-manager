package com.d9tilov.moneymanager.user.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import io.reactivex.Completable
import io.reactivex.Flowable

interface IUserLocalSource : Source {

    fun createCurrentUser(userProfile: UserProfile): Completable
    fun updateCurrentUser(userProfile: UserProfile): Completable
    fun getCurrentUser(): Flowable<UserProfile>
    fun deleteUser(): Completable
}
