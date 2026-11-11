package com.d9tilov.android.insights.data.impl

import com.d9tilov.android.insights.data.contract.InsightLanguageSource
import com.d9tilov.android.insights.domain.contract.InsightLanguageRepo
import com.d9tilov.android.user.domain.model.InsightLanguage
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow

@Inject
class InsightLanguageDataRepo(
    private val source: InsightLanguageSource,
) : InsightLanguageRepo {
    override val language: Flow<InsightLanguage> = source.language

    override suspend fun setLanguage(language: InsightLanguage) {
        source.setLanguage(language)
    }
}
