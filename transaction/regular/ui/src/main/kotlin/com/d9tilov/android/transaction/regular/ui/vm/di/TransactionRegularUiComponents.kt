package com.d9tilov.android.transaction.regular.ui.vm.di

import com.d9tilov.android.transaction.regular.ui.vm.RegularTransactionCreationViewModel
import com.d9tilov.android.transaction.regular.ui.vm.RegularTransactionListViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val transactionRegularUiComponents =
    module {
        viewModel<RegularTransactionListViewModel>()
        viewModel<RegularTransactionCreationViewModel>()
    }
