package com.d9tilov.moneymanager.billing.di

import android.app.Application
import com.d9tilov.moneymanager.billing.data.BillingDataRepo
import com.d9tilov.moneymanager.billing.data.local.BillingDataSource
import com.d9tilov.moneymanager.billing.data.local.BillingSource
import com.d9tilov.moneymanager.billing.domain.BillingInteractor
import com.d9tilov.moneymanager.billing.domain.BillingInteractorImpl
import com.d9tilov.moneymanager.billing.domain.BillingRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BillingModule {

    @Provides
    @Singleton
    fun provideBillingInteractor(billingRepo: BillingRepo): BillingInteractor = BillingInteractorImpl(billingRepo)

    @Provides
    @Singleton
    fun provideBillingLocalSource(application: Application): BillingSource =
        BillingDataSource(application)

    @Provides
    @Singleton
    fun provideBillingRepo(billingSource: BillingSource): BillingRepo =
        BillingDataRepo(billingSource)
}
