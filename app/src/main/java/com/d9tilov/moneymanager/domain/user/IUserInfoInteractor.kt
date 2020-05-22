package com.d9tilov.moneymanager.domain.user

import com.d9tilov.moneymanager.data.user.entities.UserProfile
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Flowable

interface IUserInfoInteractor {

    fun getCurrentUser(): Flowable<UserProfile>
    fun createUser(user: FirebaseUser?): Completable
    fun logout(): Completable
}