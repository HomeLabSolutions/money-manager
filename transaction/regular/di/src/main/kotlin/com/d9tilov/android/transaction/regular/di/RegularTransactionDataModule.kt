package com.d9tilov.android.transaction.regular.di

import com.d9tilov.android.transaction.regular.data.contract.RegularTransactionSource
import com.d9tilov.android.transaction.regular.data.impl.RegularTransactionDataRepo
import com.d9tilov.android.transaction.regular.data.impl.RegularTransactionLocalSource
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionRepo
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface RegularTransactionDataModule {
    @Binds
    val RegularTransactionLocalSource.provideRegularTransactionSource: RegularTransactionSource

    @Binds
    val RegularTransactionDataRepo.provideRegularTransactionRepo: RegularTransactionRepo
}
