package com.d9tilov.android.insights.presentation.di

import com.d9tilov.android.insights.presentation.InsightsViewModel
import com.d9tilov.android.insights.presentation.notification.WeeklyInsightNotificationSender
import com.d9tilov.android.insights.presentation.worker.WeeklyInsightWorker
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory
import org.koin.plugin.module.dsl.viewModel
import org.koin.plugin.module.dsl.worker

val insightsPresentationComponents =
    module {
        viewModel<InsightsViewModel>()
        factory<WeeklyInsightNotificationSender>()
        worker<WeeklyInsightWorker>()
    }
