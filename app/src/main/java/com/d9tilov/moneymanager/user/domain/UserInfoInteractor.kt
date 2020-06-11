package com.d9tilov.moneymanager.user.domain

import com.d9tilov.moneymanager.category.domain.CategoryRepo
import com.d9tilov.moneymanager.user.data.entity.UserProfile
import com.d9tilov.moneymanager.user.domain.mapper.UserDomainMapper
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Flowable

class UserInfoInteractor(
    private val userRepo: UserRepo,
    private val categoryRepo: CategoryRepo,
    private val userDomainMapper: UserDomainMapper
) : UserInteractor {

    override fun getCurrentUser(): Flowable<UserProfile> = userRepo.getUser()
    override fun createUserAndDefaultCategories(user: FirebaseUser?): Completable =
        userRepo.createUser(userDomainMapper.toDataModel(user))
            .andThen(categoryRepo.createExpenseDefaultCategories())

    override fun logout() = userRepo.logout()
}
