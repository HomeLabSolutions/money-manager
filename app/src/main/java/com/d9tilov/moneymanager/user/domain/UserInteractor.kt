package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.base.domain.Interactor
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Flowable

interface UserInteractor : Interactor {

    fun getCurrentUser(): Flowable<UserProfile>
    fun createUserAndDefaultCategories(user: FirebaseUser?): Completable
    fun logout(): Completable
}
