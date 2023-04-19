package com.d9tilov.android.billing.di

import com.d9tilov.android.billing.data.contract.BillingRepo
import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.billing.domain.impl.BillingInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BillingDomainModule {

    @Provides
    @Singleton
    fun provideBillingInteractor(billingRepo: BillingRepo): BillingInteractor =
        BillingInteractorImpl(billingRepo)
}
