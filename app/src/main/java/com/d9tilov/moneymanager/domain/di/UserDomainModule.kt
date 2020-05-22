package com.d9tilov.moneymanager.domain.di

import com.d9tilov.moneymanager.domain.category.CategoryInteractor
import com.d9tilov.moneymanager.domain.category.CategoryRepo
import com.d9tilov.moneymanager.domain.category.ICategoryInteractor
import com.d9tilov.moneymanager.domain.user.IUserInfoInteractor
import com.d9tilov.moneymanager.domain.user.UserInfoInteractor
import com.d9tilov.moneymanager.domain.user.UserRepo
import com.d9tilov.moneymanager.domain.user.mappers.DomainUserMapper
import com.d9tilov.moneymanager.settings.domain.ISettingsInteractor
import com.d9tilov.moneymanager.settings.domain.SettingsInteractor
import com.d9tilov.moneymanager.settings.domain.SettingsRepo
import com.d9tilov.moneymanager.settings.ui.vm.SettingsViewModelFactory
import com.d9tilov.moneymanager.splash.vm.SplashViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class UserDomainModule {

    @Provides
    fun provideUserInfoInteractor(
        userRepo: UserRepo,
        categoryRepo: CategoryRepo,
        userMapper: DomainUserMapper
    ): IUserInfoInteractor = UserInfoInteractor(userRepo, categoryRepo, userMapper)

    @Provides
    fun provideCategoryInteractor(categoryRepo: CategoryRepo): ICategoryInteractor =
        CategoryInteractor(categoryRepo)

    @Provides
    fun provideSettingsInteractor(settingsRepo: SettingsRepo): ISettingsInteractor =
        SettingsInteractor(settingsRepo)

    @Provides
    fun authViewModelFactory(userInfoInteractor: IUserInfoInteractor) =
        SplashViewModelFactory(userInfoInteractor)

    @Provides
    fun settingsViewModelFactory(
        userInfoInteractor: IUserInfoInteractor,
        categoryInteractor: ICategoryInteractor,
        settingsInteractor: ISettingsInteractor
    ) =
        SettingsViewModelFactory(userInfoInteractor, categoryInteractor, settingsInteractor)
}