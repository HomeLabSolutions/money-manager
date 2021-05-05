package com.d9tilov.moneymanager.user.data.local

import com.d9tilov.moneymanager.base.data.Source
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import io.reactivex.Completable
import io.reactivex.Flowable

interface UserSource : Source {

    fun createUserOrRestore(userProfile: UserProfile): Completable
    fun updateCurrentUser(userProfile: UserProfile): Completable
    fun getCurrentUser(): Flowable<UserProfile>
    fun backupUser(): Completable
    fun deleteUser(): Completable
}
