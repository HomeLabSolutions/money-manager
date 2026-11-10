package com.d9tilov.android.statistics.ui.vm.di

import com.d9tilov.android.statistics.ui.vm.StatisticsDetailsViewModel
import com.d9tilov.android.statistics.ui.vm.StatisticsViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val statisticsUiComponents =
    module {
        viewModel<StatisticsDetailsViewModel>()
        viewModel<StatisticsViewModel>()
    }
