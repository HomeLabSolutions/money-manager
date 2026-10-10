package com.d9tilov.android.insights.domain.contract

import com.d9tilov.android.insights.domain.model.GeneratedInsight
import com.d9tilov.android.insights.domain.model.Insight
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface InsightsRepo {
    fun insightPeriod(): Pair<LocalDateTime, LocalDateTime>

    fun history(): Flow<List<Insight>>

    suspend fun generate(): GeneratedInsight
}
