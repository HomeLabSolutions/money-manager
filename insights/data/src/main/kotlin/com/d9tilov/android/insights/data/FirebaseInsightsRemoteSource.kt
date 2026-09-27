package com.d9tilov.android.insights.data

import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseInsightsRemoteSource @Inject constructor() : InsightsRemoteSource {
    override suspend fun generate(
        periodStart: String,
        periodEnd: String,
        languageTag: String,
        traces: Map<String, List<Map<String, Any>>>,
    ): String {
        val text =
            FirebaseFunctions
                .getInstance()
                .getHttpsCallable("generateInsight")
                .call(
                    mapOf(
                        "periodStart" to periodStart,
                        "periodEnd" to periodEnd,
                        "language" to languageTag,
                        "traces" to traces,
                    ),
                ).await()
                .data as? String ?: error("Invalid insight response")
        return text.trim().takeIf(String::isNotEmpty) ?: error("Empty insight response")
    }
}
