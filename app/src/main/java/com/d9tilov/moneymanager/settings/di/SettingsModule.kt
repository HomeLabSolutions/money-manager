package com.d9tilov.moneymanager.settings.di

import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.settings.data.SettingsDataRepo
import com.d9tilov.moneymanager.settings.data.local.SettingsLocalSource
import com.d9tilov.moneymanager.settings.data.local.SettingsSource
import com.d9tilov.moneymanager.settings.domain.SettingsInteractor
import com.d9tilov.moneymanager.settings.domain.SettingsRepo
import com.d9tilov.moneymanager.settings.domain.SettingsInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class SettingsModule {

    @Provides
    @ActivityRetainedScoped
    fun settingsLocalSource(preferencesStore: PreferencesStore): SettingsSource =
        SettingsLocalSource(preferencesStore)

    @Provides
    @ActivityRetainedScoped
    fun settingsRepo(): SettingsRepo =
        SettingsDataRepo()

    @Provides
    @ActivityRetainedScoped
    fun provideSettingsInteractor(settingsRepo: SettingsRepo): SettingsInteractor =
        SettingsInteractorImpl(settingsRepo)
}
