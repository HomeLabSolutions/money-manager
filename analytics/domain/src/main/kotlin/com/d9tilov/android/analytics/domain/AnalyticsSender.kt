package com.d9tilov.android.analytics.domain

import com.d9tilov.android.analytics.model.AnalyticsEvent
import com.d9tilov.android.analytics.model.AnalyticsParams

interface AnalyticsSender {
    fun send(
        event: AnalyticsEvent,
        params: Map<AnalyticsParams, String?> = emptyMap(),
    )
}
