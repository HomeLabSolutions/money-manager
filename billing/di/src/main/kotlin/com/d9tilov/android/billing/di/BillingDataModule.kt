package com.d9tilov.android.billing.di

import com.d9tilov.android.billing.data.contract.BillingSource
import com.d9tilov.android.billing.data.impl.BillingDataRepo
import com.d9tilov.android.billing.data.impl.BillingDataSource
import com.d9tilov.android.billing.domain.contract.BillingRepo
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val billingDataModule =
    module {
        factory<BillingDataSource>() bind BillingSource::class
        factory<BillingDataRepo>() bind BillingRepo::class
    }
