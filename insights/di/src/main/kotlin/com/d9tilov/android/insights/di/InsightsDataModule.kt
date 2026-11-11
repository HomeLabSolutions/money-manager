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
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface InsightsDataModule {
    @Binds
    val PreferencesInsightsConsentRepo.bindInsightsConsentRepo: InsightsConsentRepo

    @Binds
    val InsightLanguageDataRepo.bindInsightLanguageRepo: InsightLanguageRepo

    @Binds
    val InsightLanguageLocalSource.bindInsightLanguageSource: InsightLanguageSource

    @Binds
    val InsightsDataRepo.bindInsightsRepo: InsightsRepo

    @Binds
    val InsightLocalSource.bindInsightSource: InsightSource

    @Binds
    val FirebaseInsightsRemoteSource.bindInsightsRemoteSource: InsightsRemoteSource

    @Binds
    val DefaultInsightsAggregator.bindInsightsAggregator: InsightsAggregator
}
