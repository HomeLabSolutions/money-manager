package com.d9tilov.android.analytics.model

sealed class AnalyticsParams(
    val name: String,
) {
    data object LoginResultProvider : AnalyticsParams("Login provider type")

    data object Exception : AnalyticsParams("")
}
