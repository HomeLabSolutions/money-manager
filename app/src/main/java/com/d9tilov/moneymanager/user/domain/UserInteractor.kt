package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.base.domain.Interactor
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface UserInteractor : Interactor {

    fun getCurrentUser(): Flowable<UserProfile>
    fun getCurrency(): Single<String>
    fun getBackupData(): Flowable<BackupData>
    fun showPrepopulate(): Single<Boolean>
    fun createUser(user: FirebaseUser?): Single<UserProfile>
    fun updateUser(userProfile: UserProfile): Completable
    fun backup(): Completable
    fun logout(): Completable
}
