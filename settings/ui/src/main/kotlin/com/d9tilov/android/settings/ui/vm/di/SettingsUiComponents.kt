package com.d9tilov.android.settings.ui.vm.di

import com.d9tilov.android.settings.ui.vm.SettingsBillingIntroViewModel
import com.d9tilov.android.settings.ui.vm.SettingsViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val settingsUiComponents =
    module {
        viewModel<SettingsBillingIntroViewModel>()
        viewModel<SettingsViewModel>()
    }
