package com.d9tilov.moneymanager.splash.di

import androidx.lifecycle.ViewModel
import com.d9tilov.moneymanager.base.di.ViewModelKey
import com.d9tilov.moneymanager.splash.vm.SplashViewModel
import com.d9tilov.moneymanager.user.domain.IUserInfoInteractor
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class SplashFragmentModule {

    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    @Provides
    fun provideSplashViewModel(userInfoInteractor: IUserInfoInteractor): ViewModel =
        SplashViewModel(userInfoInteractor)
}
