package com.d9tilov.android.insights.data

import com.d9tilov.android.database.entity.InsightDbModel
import com.d9tilov.android.insights.domain.Insight
import kotlinx.coroutines.flow.Flow

interface InsightSource {
    fun history(): Flow<List<InsightDbModel>>

    suspend fun isDailyLimitReached(): Boolean

    suspend fun save(insight: Insight)
}
