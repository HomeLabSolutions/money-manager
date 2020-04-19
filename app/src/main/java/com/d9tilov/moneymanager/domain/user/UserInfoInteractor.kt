package com.d9tilov.moneymanager.domain.user

import com.d9tilov.moneymanager.data.user.entities.UserProfile
import io.reactivex.Completable
import io.reactivex.Flowable

interface UserInfoInteractor {

    fun getCurrentUser(): Flowable<UserProfile>
    fun createUser(userProfile: UserProfile): Completable
}