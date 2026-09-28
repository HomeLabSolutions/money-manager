package com.d9tilov.android.insights.domain

import kotlinx.coroutines.flow.Flow

interface InsightLanguageRepository {
    val language: Flow<String>

    suspend fun setLanguage(language: String)
}
