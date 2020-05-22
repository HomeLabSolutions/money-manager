package com.d9tilov.moneymanager.domain.user

import com.d9tilov.moneymanager.data.user.entities.UserProfile
import com.d9tilov.moneymanager.domain.category.CategoryRepo
import com.d9tilov.moneymanager.domain.user.mappers.DomainUserMapper
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Flowable

class UserInfoInteractor(
    private val userRepo: UserRepo,
    private val categoryRepo: CategoryRepo,
    private val domainUserMapper: DomainUserMapper
) : IUserInfoInteractor {

    override fun getCurrentUser(): Flowable<UserProfile> = userRepo.getUser()
    override fun createUser(user: FirebaseUser?): Completable =
        userRepo.createUser(domainUserMapper.toDataModel(user))
            .andThen(categoryRepo.createDefaultCategories())

    override fun logout() = userRepo.logout()
}
