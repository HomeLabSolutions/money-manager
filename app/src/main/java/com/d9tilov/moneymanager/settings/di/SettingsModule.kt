package com.d9tilov.moneymanager.settings.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.d9tilov.moneymanager.backup.BackupManager
import com.d9tilov.moneymanager.backup.domain.BackupInteractor
import com.d9tilov.moneymanager.domain.user.UserInfoInteractor
import com.d9tilov.moneymanager.settings.data.SettingsRepoImpl
import com.d9tilov.moneymanager.settings.data.local.SettingsLocalSource
import com.d9tilov.moneymanager.settings.data.local.SettingsLocalSourceImpl
import com.d9tilov.moneymanager.settings.domain.SettingsInteractor
import com.d9tilov.moneymanager.settings.domain.SettingsInteractorImpl
import com.d9tilov.moneymanager.settings.domain.SettingsRepo
import com.d9tilov.moneymanager.settings.ui.SettingsFragment
import com.d9tilov.moneymanager.settings.ui.vm.SettingsViewModel
import com.d9tilov.moneymanager.settings.ui.vm.SettingsViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class SettingsModule(private val fragment: SettingsFragment) {

    @Provides
    fun provideContext(): Context = fragment.requireContext()

    @Provides
    fun provideSettingsViewModel(factory: SettingsViewModelFactory): SettingsViewModel {
        return ViewModelProvider(fragment, factory).get(SettingsViewModel::class.java)
    }

    @Provides
    fun settingsViewModelFactory(
        userInfoInteractor: UserInfoInteractor,
        backupInteractor: BackupInteractor
    ) =
        SettingsViewModelFactory(userInfoInteractor, backupInteractor)

    @Provides
    fun provideSettingsLocalSource(
        backupManager: BackupManager
    ): SettingsLocalSource =
        SettingsLocalSourceImpl(backupManager)

    @Provides
    fun provideSettingsRepo(settingsLocalSource: SettingsLocalSource): SettingsRepo =
        SettingsRepoImpl(settingsLocalSource)

    @Provides
    fun provideSettingsInteractor(settingsRepo: SettingsRepo): SettingsInteractor =
        SettingsInteractorImpl(settingsRepo)
}
