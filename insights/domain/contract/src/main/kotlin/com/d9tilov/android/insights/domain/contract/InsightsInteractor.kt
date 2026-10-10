package com.d9tilov.android.insights.domain.contract

import com.d9tilov.android.insights.domain.model.GeneratedInsight
import com.d9tilov.android.insights.domain.model.Insight
import com.d9tilov.android.user.domain.model.InsightLanguage
import kotlinx.coroutines.flow.Flow

interface InsightsInteractor {
    val language: Flow<InsightLanguage>

    suspend fun setLanguage(language: InsightLanguage)

    fun history(): Flow<List<Insight>>

    suspend fun generate(): GeneratedInsight

    suspend fun isConsentGranted(): Boolean

    suspend fun grantConsent()

    fun hasEnoughTransactionsForInsight(): Flow<Boolean>
}
