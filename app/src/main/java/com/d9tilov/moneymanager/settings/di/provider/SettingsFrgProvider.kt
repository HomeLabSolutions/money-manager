package com.d9tilov.moneymanager.settings.di.provider

import com.d9tilov.moneymanager.settings.di.SettingsFragmentModule
import com.d9tilov.moneymanager.settings.ui.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SettingsFrgProvider {

    @ContributesAndroidInjector(modules = [SettingsFragmentModule::class])
    abstract fun provideSettingsFragment(): SettingsFragment
}
