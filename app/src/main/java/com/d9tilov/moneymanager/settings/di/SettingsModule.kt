package com.d9tilov.moneymanager.settings.di

import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.settings.data.SettingsDataRepo
import com.d9tilov.moneymanager.settings.data.local.SettingsLocalSource
import com.d9tilov.moneymanager.settings.data.local.SettingsSource
import com.d9tilov.moneymanager.settings.domain.SettingsInteractor
import com.d9tilov.moneymanager.settings.domain.SettingsRepo
import com.d9tilov.moneymanager.settings.domain.UserSettingsInteractor
import dagger.Module
import dagger.Provides

@Module
class SettingsModule {

    @Provides
    fun settingsLocalSource(preferencesStore: PreferencesStore): SettingsSource =
        SettingsLocalSource(preferencesStore)

    @Provides
    fun settingsRepo(): SettingsRepo =
        SettingsDataRepo()

    @Provides
    fun provideSettingsInteractor(settingsRepo: SettingsRepo): SettingsInteractor =
        UserSettingsInteractor(settingsRepo)
}
