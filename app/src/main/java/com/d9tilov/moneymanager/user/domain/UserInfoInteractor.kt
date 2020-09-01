package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.domain.mapper.UserDomainMapper
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Flowable

class UserInfoInteractor(
    private val userRepo: UserRepo,
    private val userDomainMapper: UserDomainMapper
) : UserInteractor {

    override fun getCurrentUser(): Flowable<UserProfile> = userRepo.getUser()
    override fun createUser(user: FirebaseUser?): Completable =
        userRepo.createUser(userDomainMapper.toDataModel(user))

    override fun logout() = userRepo.logout()
}
