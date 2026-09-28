package com.d9tilov.android.insights.data

import com.d9tilov.android.database.dao.UserDao
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.insights.domain.InsightLanguageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomInsightLanguageRepository @Inject constructor(
    private val preferencesStore: PreferencesStore,
    private val userDao: UserDao,
) : InsightLanguageRepository {
    override val language: Flow<String> =
        preferencesStore.uid.filterNotNull().flatMapLatest { uid ->
            userDao.getInsightLanguage(uid).map { it.orEmpty() }
        }

    override suspend fun setLanguage(language: String) {
        require(language in setOf("", "en", "ru", "es", "pt", "ar", "hi", "zh-CN"))
        userDao.updateInsightLanguage(requireNotNull(preferencesStore.uid.first()), language)
    }
}
