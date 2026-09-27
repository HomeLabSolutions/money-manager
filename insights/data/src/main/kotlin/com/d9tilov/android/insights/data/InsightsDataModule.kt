package com.d9tilov.android.insights.data

import com.d9tilov.android.insights.domain.InsightsConsentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InsightsDataModule {
    @Binds
    fun bindInsightsConsentRepository(impl: PreferencesInsightsConsentRepository): InsightsConsentRepository
}
