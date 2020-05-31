package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Flowable

interface IUserInfoInteractor {

    fun getCurrentUser(): Flowable<UserProfile>
    fun createUser(user: FirebaseUser?): Completable
    fun logout(): Completable
}
