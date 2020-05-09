package com.d9tilov.moneymanager.domain.user.di

import com.d9tilov.moneymanager.domain.category.CategoryRepo
import com.d9tilov.moneymanager.domain.user.UserInfoInteractor
import com.d9tilov.moneymanager.domain.user.UserInfoInteractorImpl
import com.d9tilov.moneymanager.domain.user.UserRepo
import dagger.Module
import dagger.Provides

@Module
class UserDomainModule {

    @Provides
    fun provideUserInfoInteractor(
        userRepo: UserRepo,
        categoryRepo: CategoryRepo
    ): UserInfoInteractor = UserInfoInteractorImpl(userRepo,categoryRepo)
}
