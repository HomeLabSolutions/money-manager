package com.d9tilov.android.insights.di

import com.d9tilov.android.insights.domain.contract.InsightsInteractor
import com.d9tilov.android.insights.domain.impl.InsightsInteractorImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface InsightsDomainModule {
    @Binds
    val InsightsInteractorImpl.bindInsightsInteractor: InsightsInteractor
}
