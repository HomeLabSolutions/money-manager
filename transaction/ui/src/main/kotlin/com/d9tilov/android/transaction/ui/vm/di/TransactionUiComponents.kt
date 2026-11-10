package com.d9tilov.android.transaction.ui.vm.di

import com.d9tilov.android.transaction.ui.vm.TransactionInfoViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val transactionUiComponents =
    module {
        viewModel<TransactionInfoViewModel>()
    }
