package com.d9tilov.android.budget.ui.di

import com.d9tilov.android.budget.ui.BudgetAmountViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val budgetUiComponents =
    module {
        viewModel<BudgetAmountViewModel>()
    }
