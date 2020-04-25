package com.d9tilov.moneymanager.data.user.local

import com.d9tilov.moneymanager.data.base.Source
import com.d9tilov.moneymanager.data.user.entities.UserProfile
import io.reactivex.Completable
import io.reactivex.Flowable

interface UserLocalSource : Source {

    fun createCurrentUser(userProfile: UserProfile): Completable
    fun updateCurrentUser(userProfile: UserProfile): Completable
    fun getCurrentUser(): Flowable<UserProfile>
    fun deleteUser(): Completable

}