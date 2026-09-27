package com.d9tilov.android.insights.data

interface InsightsRemoteSource {
    suspend fun generate(
        periodStart: String,
        periodEnd: String,
        languageTag: String,
        traces: Map<String, List<Map<String, Any>>>,
    ): String
}
