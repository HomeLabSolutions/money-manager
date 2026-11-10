package com.d9tilov.android.insights.di

import com.d9tilov.android.insights.domain.contract.InsightsInteractor
import com.d9tilov.android.insights.domain.impl.InsightsInteractorImpl
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val insightsDomainModule =
    module {
        factory<InsightsInteractorImpl>() bind InsightsInteractor::class
    }
