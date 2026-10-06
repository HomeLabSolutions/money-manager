package com.d9tilov.android.insights.data

import com.d9tilov.android.insights.domain.InsightLanguageRepository
import com.d9tilov.android.user.domain.model.InsightLanguage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsightLanguageDataRepository @Inject constructor(
    private val source: InsightLanguageSource,
) : InsightLanguageRepository {
    override val language: Flow<InsightLanguage> = source.language

    override suspend fun setLanguage(language: InsightLanguage) {
        source.setLanguage(language)
    }
}
