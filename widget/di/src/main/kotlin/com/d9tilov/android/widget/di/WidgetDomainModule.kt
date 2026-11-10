package com.d9tilov.android.widget.di

import com.d9tilov.android.widget.domain.contract.SpendingWidgetStateObserver
import com.d9tilov.android.widget.domain.impl.spendingWidgetInteractorImplModule
import com.d9tilov.android.widget.ui.SpendingWidgetStateObserverImpl
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.single

val widgetDomainModule =
    module {
        includes(spendingWidgetInteractorImplModule)
        single<SpendingWidgetStateObserverImpl>() bind SpendingWidgetStateObserver::class
    }
