package com.d9tilov.android.insights.data.impl

import com.d9tilov.android.insights.data.contract.InsightsRemoteSource
import com.d9tilov.android.insights.data.contract.model.DailyTotalDto
import com.d9tilov.android.insights.data.contract.model.InsightResponse
import com.d9tilov.android.insights.domain.model.exception.InsightServiceException
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseInsightsRemoteSource @Inject constructor() : InsightsRemoteSource {
    override suspend fun generate(
        periodStart: String,
        periodEnd: String,
        languageTag: String,
        traces: Map<String, List<DailyTotalDto>>,
        previousInsights: List<String>,
    ): InsightResponse {
        val response =
            try {
                FirebaseFunctions
                    .getInstance()
                    .getHttpsCallable("generateInsight")
                    .call(
                        mapOf(
                            "periodStart" to periodStart,
                            "periodEnd" to periodEnd,
                            "language" to languageTag,
                            "traces" to traces.mapValues { (_, days) -> days.map(DailyTotalDto::toPayload) },
                            "previousInsights" to previousInsights,
                        ),
                    ).await()
            } catch (error: FirebaseFunctionsException) {
                when (error.code) {
                    FirebaseFunctionsException.Code.DEADLINE_EXCEEDED,
                    FirebaseFunctionsException.Code.INTERNAL,
                    FirebaseFunctionsException.Code.UNAVAILABLE,
                    -> throw InsightServiceException(error)

                    else -> throw error
                }
            }
        val text = response.data as? String ?: error("Invalid insight response")
        return InsightResponse(
            text = text.trim().takeIf(String::isNotEmpty) ?: error("Empty insight response"),
        )
    }
}
