package com.d9tilov.android.insights.data.impl

import com.d9tilov.android.core.utils.currentDate
import com.d9tilov.android.core.utils.getEndOfDay
import com.d9tilov.android.core.utils.getStartOfDay
import com.d9tilov.android.database.dao.InsightDao
import com.d9tilov.android.database.entity.InsightDbModel
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.insights.data.contract.InsightSource
import com.d9tilov.android.insights.data.impl.mapper.toDbModel
import com.d9tilov.android.insights.domain.model.Insight
import com.d9tilov.android.insights.domain.model.InsightsConstants.INSIGHT_WINDOW_DAYS
import com.d9tilov.android.insights.domain.model.InsightsConstants.MAX_INSIGHTS_PER_DAY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class InsightLocalSource @Inject constructor(
    private val dao: InsightDao,
    private val preferencesStore: PreferencesStore,
) : InsightSource {
    override fun history(): Flow<List<InsightDbModel>> =
        preferencesStore.uid.filterNotNull().flatMapLatest { clientId ->
            dao.getAll(clientId)
        }

    override suspend fun isDailyLimitReached(): Boolean {
        val clientId = requireNotNull(preferencesStore.uid.first())
        val today = currentDate()
        val start = today.getStartOfDay()
        val end = today.getEndOfDay()
        return dao.get(clientId, start, end).first().size >= MAX_INSIGHTS_PER_DAY
    }

    override suspend fun save(insight: Insight) {
        val clientId = requireNotNull(preferencesStore.uid.first())
        dao.upsertAndKeepLatest(
            insight.toDbModel(clientId),
            INSIGHT_WINDOW_DAYS,
        )
    }
}
