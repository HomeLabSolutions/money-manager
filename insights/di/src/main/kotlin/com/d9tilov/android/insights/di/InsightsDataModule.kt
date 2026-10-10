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
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InsightsDataModule {
    @Binds
    fun bindInsightsConsentRepo(impl: PreferencesInsightsConsentRepo): InsightsConsentRepo

    @Binds
    fun bindInsightLanguageRepo(impl: InsightLanguageDataRepo): InsightLanguageRepo

    @Binds
    fun bindInsightLanguageSource(impl: InsightLanguageLocalSource): InsightLanguageSource

    @Binds
    fun bindInsightsRepo(impl: InsightsDataRepo): InsightsRepo

    @Binds
    fun bindInsightSource(impl: InsightLocalSource): InsightSource

    @Binds
    fun bindInsightsRemoteSource(impl: FirebaseInsightsRemoteSource): InsightsRemoteSource

    @Binds
    fun bindInsightsAggregator(impl: DefaultInsightsAggregator): InsightsAggregator
}
