package com.d9tilov.android.insights.di

import com.d9tilov.android.insights.domain.contract.InsightsInteractor
import com.d9tilov.android.insights.domain.impl.InsightsInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface InsightsDomainModule {
    @Binds
    fun bindInsightsInteractor(impl: InsightsInteractorImpl): InsightsInteractor
}
