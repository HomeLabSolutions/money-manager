package com.d9tilov.android.insights.data.contract

import com.d9tilov.android.user.domain.model.InsightLanguage
import kotlinx.coroutines.flow.Flow

interface InsightLanguageSource {
    val language: Flow<InsightLanguage>

    suspend fun setLanguage(language: InsightLanguage)
}
