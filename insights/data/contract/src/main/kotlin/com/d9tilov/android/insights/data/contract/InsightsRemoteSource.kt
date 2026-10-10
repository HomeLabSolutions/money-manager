package com.d9tilov.android.insights.data.contract

import com.d9tilov.android.insights.data.contract.model.DailyTotalDto
import com.d9tilov.android.insights.data.contract.model.InsightResponse

interface InsightsRemoteSource {
    suspend fun generate(
        periodStart: String,
        periodEnd: String,
        languageTag: String,
        traces: Map<String, List<DailyTotalDto>>,
        previousInsights: List<String>,
    ): InsightResponse
}
