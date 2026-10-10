package com.d9tilov.android.insights.data.impl

import com.d9tilov.android.database.dao.UserDao
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.insights.data.contract.InsightLanguageSource
import com.d9tilov.android.user.domain.model.InsightLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InsightLanguageLocalSource @Inject constructor(
    private val preferencesStore: PreferencesStore,
    private val userDao: UserDao,
) : InsightLanguageSource {
    override val language: Flow<InsightLanguage> =
        preferencesStore.uid.filterNotNull().flatMapLatest { uid ->
            userDao.getInsightLanguage(uid).map(InsightLanguage::fromTag)
        }

    override suspend fun setLanguage(language: InsightLanguage) {
        userDao.updateInsightLanguage(requireNotNull(preferencesStore.uid.first()), language.tag)
    }
}
