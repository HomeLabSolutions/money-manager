package com.d9tilov.android.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.d9tilov.android.core.constants.DataConstants.DATA_STORE_NAME
import com.d9tilov.android.core.constants.DataConstants.PREFERENCE_CLIENT_UID
import com.d9tilov.android.core.constants.DataConstants.PREFERENCE_LAST_BACKUP_DATE
import com.d9tilov.android.core.constants.DataConstants.STORE_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    DATA_STORE_NAME,
    produceMigrations = { context -> listOf(SharedPreferencesMigration(context, STORE_NAME)) },
)

class PreferencesStore(
    context: Context,
) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    val uid: Flow<String?> = dataStore.data.map { data -> data[PREFERENCE_CLIENT_UID_KEY] }

    suspend fun updateUid(uid: String) {
        dataStore.edit { preferences -> preferences[PREFERENCE_CLIENT_UID_KEY] = uid }
    }

    val backupData: Flow<Long> =
        dataStore.data.map { data -> data[PREFERENCE_LAST_BACKUP_DATE_KEY] ?: -1L }

    suspend fun updateLastBackupDate(date: Long) {
        dataStore.edit { preferences -> preferences[PREFERENCE_LAST_BACKUP_DATE_KEY] = date }
    }

    suspend fun clearAllData() {
        dataStore.edit { it.clear() }
    }

    companion object {
        private val PREFERENCE_LAST_BACKUP_DATE_KEY =
            longPreferencesKey(PREFERENCE_LAST_BACKUP_DATE)
        private val PREFERENCE_CLIENT_UID_KEY = stringPreferencesKey(PREFERENCE_CLIENT_UID)
    }
}
