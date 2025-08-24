package com.d9tilov.android.analytics.model

sealed class AnalyticsEvent(
    val name: String,
) {
    sealed class Internal(
        name: String,
    ) : AnalyticsEvent(name) {
        data object Screen : Internal("screen_name")

        data object Backup : Internal("backup")
    }

    sealed class Client(
        name: String,
    ) : AnalyticsEvent(name) {
        data object Auth : Internal("auth")
    }
}
