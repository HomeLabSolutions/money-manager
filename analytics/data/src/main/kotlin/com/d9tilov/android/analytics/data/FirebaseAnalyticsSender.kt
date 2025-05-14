package com.d9tilov.android.analytics.data

import com.d9tilov.android.analytics.domain.AnalyticsSender
import com.d9tilov.android.analytics.model.AnalyticsEvent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent

class FirebaseAnalyticsSender(
    private val firebaseAnalytics: FirebaseAnalytics,
) : AnalyticsSender {
    override fun send(event: AnalyticsEvent) = firebaseAnalytics.logEvent(event.name, null)

    override fun sendWithParams(
        event: AnalyticsEvent,
        paramsBuilder: MutableMap<String, Any>.() -> Unit,
    ) = firebaseAnalytics.logEvent(event.name) {
        val params = mutableMapOf<String, Any>().apply(paramsBuilder)
        params.forEach { (key, value) ->
            when (value) {
                is Double -> param(key, value)
                is Long -> param(key, value)
                is String -> param(key, value)
                is Int -> param(key, value.toLong())
            }
        }
    }
}
