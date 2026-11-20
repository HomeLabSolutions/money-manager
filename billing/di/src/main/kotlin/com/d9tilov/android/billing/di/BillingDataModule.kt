package com.d9tilov.android.billing.di

import com.d9tilov.android.billing.data.contract.BillingSource
import com.d9tilov.android.billing.data.impl.BillingDataRepo
import com.d9tilov.android.billing.data.impl.BillingDataSource
import com.d9tilov.android.billing.domain.contract.BillingRepo
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface BillingDataModule {
    @Binds
    val BillingDataSource.provideBillingLocalSource: BillingSource

    @Binds
    val BillingDataRepo.provideBillingRepo: BillingRepo
}
