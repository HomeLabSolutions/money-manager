package com.d9tilov.moneymanager.billing.di

import android.app.Application
import com.d9tilov.moneymanager.billing.data.BillingDataRepo
import com.d9tilov.moneymanager.billing.data.local.BillingDataSource
import com.d9tilov.moneymanager.billing.data.local.BillingSource
import com.d9tilov.moneymanager.billing.domain.BillingInteractor
import com.d9tilov.moneymanager.billing.domain.BillingInteractorImpl
import com.d9tilov.moneymanager.billing.domain.BillingRepo
import com.d9tilov.moneymanager.billing.domain.entity.BillingSkuDetails.Companion.SKU_SUBSCRIPTION_ANNUAL
import com.d9tilov.moneymanager.billing.domain.entity.BillingSkuDetails.Companion.SKU_SUBSCRIPTION_QUARTERLY
import com.d9tilov.moneymanager.currency.domain.mapper.CurrencyDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.GlobalScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BillingModule {

    @Provides
    @Singleton
    fun provideBillingInteractor(
        billingRepo: BillingRepo,
        currencyDomainMapper: CurrencyDomainMapper
    ): BillingInteractor =
        BillingInteractorImpl(billingRepo, currencyDomainMapper)

    @Provides
    @Singleton
    fun provideBillingLocalSource(application: Application): BillingSource =
        BillingDataSource(
            application,
            GlobalScope,
            listOf(SKU_SUBSCRIPTION_QUARTERLY, SKU_SUBSCRIPTION_ANNUAL)
        )

    @Provides
    @Singleton
    fun provideBillingRepo(billingSource: BillingSource): BillingRepo =
        BillingDataRepo(billingSource, GlobalScope)
}
