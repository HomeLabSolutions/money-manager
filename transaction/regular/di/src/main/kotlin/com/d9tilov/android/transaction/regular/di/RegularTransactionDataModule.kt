package com.d9tilov.android.transaction.regular.di

import com.d9tilov.android.transaction.regular.data.contract.RegularTransactionSource
import com.d9tilov.android.transaction.regular.data.impl.RegularTransactionDataRepo
import com.d9tilov.android.transaction.regular.data.impl.RegularTransactionLocalSource
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionRepo
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val regularTransactionDataModule =
    module {
        factory<RegularTransactionLocalSource>() bind RegularTransactionSource::class
        factory<RegularTransactionDataRepo>() bind RegularTransactionRepo::class
    }
