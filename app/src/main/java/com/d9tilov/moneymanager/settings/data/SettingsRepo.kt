package com.d9tilov.moneymanager.settings.data

import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.settings.domain.ISettingsRepo

class SettingsRepo(private val preferencesStore: PreferencesStore) : ISettingsRepo {
    override fun saveNumber() {
        preferencesStore.number = 5
    }

    override fun restoreNumber(): Int = preferencesStore.number
}
