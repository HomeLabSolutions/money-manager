package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface UserRepo {
    fun getUser(): Flowable<UserProfile>
    fun getCurrency(): Single<String>
    fun getBackupData(): Flowable<BackupData>
    fun showPrepopulate(): Single<Boolean>
    fun createUser(entity: UserProfile): Completable
    fun updateUser(entity: UserProfile): Completable
    fun backup(): Completable
    fun logout(): Completable
}
