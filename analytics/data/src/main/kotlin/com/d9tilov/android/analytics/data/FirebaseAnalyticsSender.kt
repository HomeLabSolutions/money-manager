package com.d9tilov.android.analytics.data

import com.d9tilov.android.analytics.constants.LOG_TAG
import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.analytics.model.AnalyticsEvent
import com.d9tilov.android.analytics.model.AnalyticsParams
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import timber.log.Timber

class FirebaseAnalyticsSender(
    private val firebaseAnalytics: FirebaseAnalytics,
) : AnalyticsSender {
    override fun send(
        event: AnalyticsEvent,
        params: Map<AnalyticsParams, String?>,
    ) {
        Timber.tag(LOG_TAG).i("Event: ${event.name}, params: $params")
        firebaseAnalytics.logEvent(event.name) {
            params.forEach { (key, value) ->
                value?.let { param(key.name, it) }
            }
        }
    }
}
