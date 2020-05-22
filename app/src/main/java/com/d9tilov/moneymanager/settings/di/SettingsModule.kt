package com.d9tilov.moneymanager.settings.di

import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.settings.data.SettingsRepo
import com.d9tilov.moneymanager.settings.domain.ISettingsInteractor
import com.d9tilov.moneymanager.settings.domain.ISettingsRepo
import com.d9tilov.moneymanager.settings.domain.SettingsInteractor
import dagger.Module
import dagger.Provides

@Module
class SettingsModule {

    @Provides
    fun settingsRepo(preferencesStore: PreferencesStore): ISettingsRepo =
        SettingsRepo(preferencesStore)

    @Provides
    fun provideSettingsInteractor(settingsRepo: ISettingsRepo): ISettingsInteractor =
        SettingsInteractor(settingsRepo)
}
