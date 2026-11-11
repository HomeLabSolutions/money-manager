package com.d9tilov.android.transaction.di

import com.d9tilov.android.transaction.data.contract.TransactionSource
import com.d9tilov.android.transaction.data.impl.TransactionDataRepo
import com.d9tilov.android.transaction.data.impl.TransactionLocalSource
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface TransactionDataModule {
    @Binds
    val TransactionLocalSource.provideTransactionLocalSource: TransactionSource

    @Binds
    val TransactionDataRepo.provideTransactionRepo: TransactionRepo
}
