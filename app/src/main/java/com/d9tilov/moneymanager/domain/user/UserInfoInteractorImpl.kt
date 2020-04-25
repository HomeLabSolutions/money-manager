package com.d9tilov.moneymanager.domain.user

import com.d9tilov.moneymanager.data.user.entities.UserProfile
import io.reactivex.Flowable

class UserInfoInteractorImpl constructor(private val userRepo: UserRepo) : UserInfoInteractor {
    override fun getCurrentUser(): Flowable<UserProfile> = userRepo.getUser()

    override fun createUser(userProfile: UserProfile) = userRepo.createUser(userProfile)

    override fun logout() = userRepo.logout()
}