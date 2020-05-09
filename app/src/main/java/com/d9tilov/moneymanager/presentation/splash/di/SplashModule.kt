package com.d9tilov.moneymanager.presentation.splash.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.d9tilov.moneymanager.domain.user.UserInfoInteractor
import com.d9tilov.moneymanager.domain.user.mappers.DomainUserMapper
import com.d9tilov.moneymanager.presentation.splash.ui.SplashActivity
import com.d9tilov.moneymanager.presentation.splash.vm.SplashViewModel
import com.d9tilov.moneymanager.presentation.splash.vm.SplashViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SplashModule(private val activity: SplashActivity) {

    @Provides
    fun provideContext(): Context = activity

    @Provides
    fun splashViewModel(factory: SplashViewModelFactory): SplashViewModel {
        return ViewModelProvider(activity, factory).get(SplashViewModel::class.java)
    }

    @Provides
    fun authViewModelFactory(userInfoInteractor: UserInfoInteractor, userMapper: DomainUserMapper) =
        SplashViewModelFactory(userInfoInteractor, userMapper)

}