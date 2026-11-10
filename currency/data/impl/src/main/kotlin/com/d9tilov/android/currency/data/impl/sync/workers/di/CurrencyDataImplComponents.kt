package com.d9tilov.android.currency.data.impl.sync.workers.di

import com.d9tilov.android.currency.data.impl.sync.workers.CurrencySyncWorker
import org.koin.dsl.module
import org.koin.plugin.module.dsl.worker

val currencyDataImplComponents =
    module {
        worker<CurrencySyncWorker>()
    }
