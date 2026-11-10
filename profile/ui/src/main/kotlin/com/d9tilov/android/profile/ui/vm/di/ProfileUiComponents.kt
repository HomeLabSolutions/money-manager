package com.d9tilov.android.profile.ui.vm.di

import com.d9tilov.android.profile.ui.vm.ProfileViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val profileUiComponents =
    module {
        viewModel<ProfileViewModel>()
    }
