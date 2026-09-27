package com.d9tilov.android.insights.domain

interface InsightsRepository {
    suspend fun generate(languageTag: String): String
}
