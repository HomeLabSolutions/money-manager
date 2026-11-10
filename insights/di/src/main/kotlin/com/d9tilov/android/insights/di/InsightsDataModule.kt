package com.d9tilov.android.insights.di

import com.d9tilov.android.insights.data.contract.InsightLanguageSource
import com.d9tilov.android.insights.data.contract.InsightSource
import com.d9tilov.android.insights.data.contract.InsightsAggregator
import com.d9tilov.android.insights.data.contract.InsightsRemoteSource
import com.d9tilov.android.insights.data.impl.DefaultInsightsAggregator
import com.d9tilov.android.insights.data.impl.FirebaseInsightsRemoteSource
import com.d9tilov.android.insights.data.impl.InsightLanguageDataRepo
import com.d9tilov.android.insights.data.impl.InsightLanguageLocalSource
import com.d9tilov.android.insights.data.impl.InsightLocalSource
import com.d9tilov.android.insights.data.impl.InsightsDataRepo
import com.d9tilov.android.insights.data.impl.PreferencesInsightsConsentRepo
import com.d9tilov.android.insights.domain.contract.InsightLanguageRepo
import com.d9tilov.android.insights.domain.contract.InsightsConsentRepo
import com.d9tilov.android.insights.domain.contract.InsightsRepo
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory
import org.koin.plugin.module.dsl.single

val insightsDataModule =
    module {
        factory<PreferencesInsightsConsentRepo>() bind InsightsConsentRepo::class
        factory<InsightLanguageDataRepo>() bind InsightLanguageRepo::class
        factory<InsightLanguageLocalSource>() bind InsightLanguageSource::class
        single<InsightsDataRepo>() bind InsightsRepo::class
        factory<InsightLocalSource>() bind InsightSource::class
        factory<FirebaseInsightsRemoteSource>() bind InsightsRemoteSource::class
        factory<DefaultInsightsAggregator>() bind InsightsAggregator::class
    }
