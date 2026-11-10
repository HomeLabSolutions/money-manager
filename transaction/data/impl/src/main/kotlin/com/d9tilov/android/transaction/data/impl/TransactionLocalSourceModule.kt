package com.d9tilov.android.transaction.data.impl

import com.d9tilov.android.transaction.data.contract.TransactionSource
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

// Keep constructor wiring beside the class so parameter qualifiers are available to the compiler plugin.
val transactionLocalSourceModule =
    module {
        factory<TransactionLocalSource>() bind TransactionSource::class
    }
