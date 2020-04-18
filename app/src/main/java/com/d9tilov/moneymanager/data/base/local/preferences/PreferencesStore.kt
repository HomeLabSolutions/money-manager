package com.d9tilov.moneymanager.data.base.local.preferences

import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesStore @Inject constructor(private val context: Context) {

    companion object {
        private const val STORE_NAME = "MoneyManagerPreferencesStore"
        private const val BASE_NAMESPACE = "com.d9tilov.moneymanager"
        const val PREFERENCE_CLIENT_ID = BASE_NAMESPACE + "current.client.id"
    }

    private val sharedPreferences = context.getSharedPreferences(
        STORE_NAME,
        Context.MODE_PRIVATE
    )

    var clientId: String?
        get() = sharedPreferences.getString(
            PREFERENCE_CLIENT_ID,
            null
        )
        set(clientId) {
            sharedPreferences.edit {
                putString(PREFERENCE_CLIENT_ID, clientId)
            }
        }
}