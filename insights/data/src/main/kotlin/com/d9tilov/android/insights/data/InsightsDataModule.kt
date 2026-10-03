package com.d9tilov.android.insights.data

import com.d9tilov.android.insights.domain.InsightLanguageRepository
import com.d9tilov.android.insights.domain.InsightsConsentRepository
import com.d9tilov.android.insights.domain.InsightsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InsightsDataModule {
    @Binds
    fun bindInsightsConsentRepository(impl: PreferencesInsightsConsentRepository): InsightsConsentRepository

    @Binds
    fun bindInsightLanguageRepository(impl: RoomInsightLanguageRepository): InsightLanguageRepository

    @Binds
    fun bindInsightsRepository(impl: InsightsDataRepo): InsightsRepository

    @Binds
    fun bindInsightsRemoteSource(impl: FirebaseInsightsRemoteSource): InsightsRemoteSource
}
