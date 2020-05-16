package com.d9tilov.moneymanager.settings.di.provider

import com.d9tilov.moneymanager.settings.ui.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsFrgProvider {

    @ContributesAndroidInjector
    abstract fun provideSettingsFragment(): SettingsFragment
}
