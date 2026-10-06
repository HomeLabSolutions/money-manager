package com.d9tilov.android.insights.domain

import com.d9tilov.android.user.domain.model.InsightLanguage
import kotlinx.coroutines.flow.Flow

interface InsightLanguageRepository {
    val language: Flow<InsightLanguage>

    suspend fun setLanguage(language: InsightLanguage)
}
