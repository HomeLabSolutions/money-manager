package com.d9tilov.moneymanager.settings.data

import com.d9tilov.moneymanager.data.base.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.settings.domain.SettingsRepo

class SettingsRepoImpl(private val preferencesStore: PreferencesStore) : SettingsRepo {
    override fun saveNumber() {
        preferencesStore.number = 5
    }

    override fun restoreNumber(): Int = preferencesStore.number
}
