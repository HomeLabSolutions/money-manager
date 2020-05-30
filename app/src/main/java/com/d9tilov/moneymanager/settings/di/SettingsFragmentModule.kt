package com.d9tilov.moneymanager.settings.di

import androidx.lifecycle.ViewModel
import com.d9tilov.moneymanager.base.di.ViewModelKey
import com.d9tilov.moneymanager.category.domain.ICategoryInteractor
import com.d9tilov.moneymanager.settings.domain.ISettingsInteractor
import com.d9tilov.moneymanager.settings.ui.vm.SettingsViewModel
import com.d9tilov.moneymanager.user.domain.IUserInfoInteractor
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class SettingsFragmentModule {

    @Provides
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun provideSettingsViewModel(
        userInfoInteractor: IUserInfoInteractor,
        categoryInteractor: ICategoryInteractor,
        settingsInteractor: ISettingsInteractor
    ): ViewModel =
        SettingsViewModel(userInfoInteractor, categoryInteractor, settingsInteractor)
}
