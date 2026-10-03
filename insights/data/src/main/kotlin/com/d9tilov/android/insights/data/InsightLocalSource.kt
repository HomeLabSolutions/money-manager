package com.d9tilov.android.insights.data

import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.core.utils.toMillis
import com.d9tilov.android.database.dao.InsightDao
import com.d9tilov.android.database.entity.InsightDbModel
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import java.util.Locale
import javax.inject.Inject

internal const val INSIGHT_WINDOW_DAYS = 90
private const val MAX_STORED_INSIGHTS = INSIGHT_WINDOW_DAYS
internal const val MAX_INSIGHT_TEXT_LENGTH = 1000

internal fun matchesPreviousInsight(
    insight: String,
    previous: List<String>,
): Boolean {
    fun normalize(text: String): String = text.trim().replace(Regex("\\s+"), " ").lowercase(Locale.ROOT)
    val normalized = normalize(insight)
    return previous.any { normalize(it) == normalized }
}

class InsightLocalSource @Inject constructor(
    private val dao: InsightDao,
) {
    fun history(clientId: String): Flow<List<InsightDbModel>> = dao.observeHistory(clientId)

    suspend fun hasInsightToday(clientId: String): Boolean {
        val today = currentDate()
        val start = today.getStartOfDay().toMillis()
        val end = today.plus(1, DateTimeUnit.DAY).getStartOfDay().toMillis()
        return dao.hasInsightInPeriod(clientId, start, end)
    }

    suspend fun save(
        clientId: String,
        text: String,
    ) {
        dao.upsertAndKeepLatest(
            InsightDbModel(clientId = clientId, createdAtMillis = System.currentTimeMillis(), text = text),
            MAX_STORED_INSIGHTS,
        )
    }
}
