package com.d9tilov.android.billing.di

import com.d9tilov.android.billing.data.contract.BillingSource
import com.d9tilov.android.billing.data.impl.BillingDataRepo
import com.d9tilov.android.billing.data.impl.BillingDataSource
import com.d9tilov.android.billing.domain.contract.BillingRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BillingDataModule {
    @Binds
    fun provideBillingLocalSource(impl: BillingDataSource): BillingSource

    @Binds
    fun provideBillingRepo(impl: BillingDataRepo): BillingRepo
}
