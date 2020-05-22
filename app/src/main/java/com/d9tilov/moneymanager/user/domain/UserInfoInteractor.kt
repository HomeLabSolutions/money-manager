package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.category.ICategoryRepo
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.mapper.DomainUserMapper
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Flowable

class UserInfoInteractor(
    private val userRepo: IUserRepo,
    private val categoryRepo: ICategoryRepo,
    private val domainUserMapper: DomainUserMapper
) : IUserInfoInteractor {

    override fun getCurrentUser(): Flowable<UserProfile> = userRepo.getUser()
    override fun createUser(user: FirebaseUser?): Completable =
        userRepo.createUser(domainUserMapper.toDataModel(user))
            .andThen(categoryRepo.createDefaultCategories())

    override fun logout() = userRepo.logout()
}
