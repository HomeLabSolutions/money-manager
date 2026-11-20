package com.d9tilov.android.billing.di

import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.billing.domain.impl.BillingInteractorImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface BillingDomainModule {
    @Binds
    val BillingInteractorImpl.provideBillingInteractor: BillingInteractor
}
