package com.d9tilov.moneymanager.domain.user

import com.d9tilov.moneymanager.data.user.entities.UserProfile
import com.d9tilov.moneymanager.domain.category.CategoryRepo
import io.reactivex.Completable
import io.reactivex.Flowable

class UserInfoInteractorImpl constructor(
    private val userRepo: UserRepo,
    private val categoryRepo: CategoryRepo
) : UserInfoInteractor {

    override fun getCurrentUser(): Flowable<UserProfile> = userRepo.getUser()
    override fun createUser(userProfile: UserProfile): Completable =
        userRepo.createUser(userProfile)
            .andThen(categoryRepo.createDefaultCategories())

    override fun logout() = userRepo.logout()
}
