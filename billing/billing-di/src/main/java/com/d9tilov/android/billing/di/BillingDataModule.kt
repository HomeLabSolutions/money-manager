package com.d9tilov.android.billing.di

import android.app.Application
import com.d9tilov.android.billing.domain.contract.BillingRepo
import com.d9tilov.android.billing.data.contract.BillingSource
import com.d9tilov.android.billing.data.impl.BillingDataRepo
import com.d9tilov.android.billing.data.impl.BillingDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BillingDataModule {

    @Provides
    @Singleton
    fun provideBillingLocalSource(application: Application): BillingSource =
        BillingDataSource(application)

    @Provides
    @Singleton
    fun provideBillingRepo(billingSource: BillingSource): BillingRepo =
        BillingDataRepo(billingSource)
}
