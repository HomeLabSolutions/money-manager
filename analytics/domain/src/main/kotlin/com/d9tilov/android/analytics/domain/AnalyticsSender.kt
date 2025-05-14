package com.d9tilov.android.analytics.domain

import com.d9tilov.android.analytics.model.AnalyticsEvent

interface AnalyticsSender {
    fun send(event: AnalyticsEvent)

    fun sendWithParams(
        event: AnalyticsEvent,
        paramsBuilder: MutableMap<String, Any>.() -> Unit,
    )
}
