package com.d9tilov.android.billing.di

import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.billing.domain.impl.BillingInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BillingDomainModule {
    @Binds
    fun provideBillingInteractor(impl: BillingInteractorImpl): BillingInteractor
}
