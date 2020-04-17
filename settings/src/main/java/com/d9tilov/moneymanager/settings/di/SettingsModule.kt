package com.d9tilov.moneymanager.settings.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.d9tilov.moneymanager.settings.ui.SettingsFragment
import com.d9tilov.moneymanager.settings.vm.SettingsViewModel
import com.d9tilov.moneymanager.settings.vm.SettingsViewModelFactory
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
    fun settingsViewModelFactory() = SettingsViewModelFactory()
}
