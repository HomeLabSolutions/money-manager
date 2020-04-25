package com.d9tilov.moneymanager.data.base.local.preferences

import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesStore @Inject constructor(context: Context) {

    companion object {
        private const val STORE_NAME = "MoneyManagerPreferencesStore"
        private const val BASE_NAMESPACE = "com.d9tilov.moneymanager"
        const val PREFERENCE_CLIENT_UID = BASE_NAMESPACE + "current.client.uid"
    }

    private val sharedPreferences = context.getSharedPreferences(
        STORE_NAME,
        Context.MODE_PRIVATE
    )

    var uid: String?
        get() = sharedPreferences.getString(
            PREFERENCE_CLIENT_UID,
            null
        )
        set(clientUid) {
            sharedPreferences.edit {
                putString(PREFERENCE_CLIENT_UID, clientUid)
            }
        }

    fun clearAllData() {
        val prefs: Map<String, *> = sharedPreferences.all
        for ((key) in prefs) {
            sharedPreferences.edit().remove(key).apply()
        }
    }
}