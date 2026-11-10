package com.d9tilov.moneymanager.home.di

import com.d9tilov.moneymanager.home.MainViewModel
import com.d9tilov.moneymanager.logout.AppLogoutHandler
import com.d9tilov.moneymanager.prepopulate.PrepopulateViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory
import org.koin.plugin.module.dsl.viewModel

val appComponents =
    module {
        viewModel<MainViewModel>()
        factory<AppLogoutHandler>()
        viewModel<PrepopulateViewModel>()
    }
