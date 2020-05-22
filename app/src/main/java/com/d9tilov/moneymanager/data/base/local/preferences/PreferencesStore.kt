package com.d9tilov.moneymanager.data.base.local.preferences

import android.content.Context
import com.d9tilov.moneymanager.core.util.int
import com.d9tilov.moneymanager.core.util.stringNullable
import javax.inject.Inject

class PreferencesStore @Inject constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        STORE_NAME,
        Context.MODE_PRIVATE
    )

    var uid by sharedPreferences.stringNullable(key = { PREFERENCE_CLIENT_UID })
    var number by sharedPreferences.int(key = { PREFERENCE_SAVE })

    fun clearAllData() {
        val prefs: Map<String, *> = sharedPreferences.all
        for ((key) in prefs) {
            sharedPreferences.edit().remove(key).apply()
        }
    }

    companion object {
        const val STORE_NAME = "MoneyManagerPreferencesStore"
        private const val BASE_NAMESPACE = "com.d9tilov.moneymanager"
        const val PREFERENCE_CLIENT_UID = BASE_NAMESPACE + "current.client.uid"
        const val PREFERENCE_SAVE = BASE_NAMESPACE + "save"
    }

}