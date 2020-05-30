package com.d9tilov.moneymanager.splash.ui.provider

import com.d9tilov.moneymanager.splash.di.SplashFragmentModule
import com.d9tilov.moneymanager.splash.ui.SplashFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SplashFrgProvider {
    @ContributesAndroidInjector(modules = [SplashFragmentModule::class])
    internal abstract fun provideSplashFragment(): SplashFragment
}
