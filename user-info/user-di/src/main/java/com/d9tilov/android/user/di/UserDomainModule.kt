package com.d9tilov.android.user.di

import com.d9tilov.android.user.domain.contract.UserInteractor
import com.d9tilov.android.user.domain.contract.UserRepo
import com.d9tilov.android.user.domain.impl.UserInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UserDomainModule {

    @Provides
    fun provideUserInteractor(userRepo: UserRepo): UserInteractor = UserInteractorImpl(userRepo)
}
