package com.d9tilov.android.incomeexpense.ui.vm.di

import com.d9tilov.android.incomeexpense.ui.vm.IncomeExpenseViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val incomeexpenseUiComponents =
    module {
        viewModel<IncomeExpenseViewModel>()
    }
