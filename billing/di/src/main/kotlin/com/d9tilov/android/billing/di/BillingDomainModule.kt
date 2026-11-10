package com.d9tilov.android.billing.di

import com.d9tilov.android.billing.domain.contract.BillingInteractor
import com.d9tilov.android.billing.domain.impl.BillingInteractorImpl
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val billingDomainModule =
    module {
        factory<BillingInteractorImpl>() bind BillingInteractor::class
    }
