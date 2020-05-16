package com.d9tilov.moneymanager.domain.di

import com.d9tilov.moneymanager.domain.user.UserInfoInteractor
import com.d9tilov.moneymanager.domain.user.mappers.DomainUserMapper
import com.d9tilov.moneymanager.splash.vm.SplashViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class UserDomainModule {

    @Provides
    fun authViewModelFactory(userInfoInteractor: UserInfoInteractor, userMapper: DomainUserMapper) =
        SplashViewModelFactory(userInfoInteractor, userMapper)
}