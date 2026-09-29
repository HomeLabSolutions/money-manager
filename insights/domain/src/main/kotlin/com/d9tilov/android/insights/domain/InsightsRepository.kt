package com.d9tilov.android.insights.domain

import kotlinx.coroutines.flow.Flow

interface InsightsRepository {
    fun history(): Flow<List<Insight>>

    suspend fun generate(languageTag: String): String
}
