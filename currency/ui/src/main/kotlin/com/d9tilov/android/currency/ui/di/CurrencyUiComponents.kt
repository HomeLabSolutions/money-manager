package com.d9tilov.android.currency.ui.di

import com.d9tilov.android.currency.ui.CurrencyViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.viewModel

val currencyUiComponents =
    module {
        viewModel<CurrencyViewModel>()
    }
