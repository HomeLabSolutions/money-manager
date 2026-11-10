package com.d9tilov.android.transaction.di

import com.d9tilov.android.transaction.data.impl.TransactionDataRepo
import com.d9tilov.android.transaction.data.impl.transactionLocalSourceModule
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val transactionDataModule =
    module {
        includes(transactionLocalSourceModule)
        factory<TransactionDataRepo>() bind TransactionRepo::class
    }
