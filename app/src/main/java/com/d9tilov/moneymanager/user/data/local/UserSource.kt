package com.d9tilov.moneymanager.user.data.local

import com.d9tilov.moneymanager.backup.BackupData
import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface UserSource : Source {

    fun createUserOrRestore(userProfile: UserProfile): Completable
    fun updateCurrentUser(userProfile: UserProfile): Completable
    fun showPrepopulate(): Single<Boolean>
    fun getBackupData(): Flowable<BackupData>
    fun getCurrentUser(): Flowable<UserProfile>
    fun getCurrency(): Single<String>
    fun backupUser(): Completable
    fun deleteUser(): Completable
}
