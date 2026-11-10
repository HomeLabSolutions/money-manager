package com.d9tilov.android.widget.domain.impl

import com.d9tilov.android.widget.domain.contract.SpendingWidgetInteractor
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

// Keep constructor wiring beside the class so parameter qualifiers are available to the compiler plugin.
val spendingWidgetInteractorImplModule =
    module {
        factory<SpendingWidgetInteractorImpl>() bind SpendingWidgetInteractor::class
    }
