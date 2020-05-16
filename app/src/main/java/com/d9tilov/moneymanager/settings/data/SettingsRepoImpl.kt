package com.d9tilov.moneymanager.settings.data

import com.d9tilov.moneymanager.settings.data.local.SettingsLocalSource
import com.d9tilov.moneymanager.settings.domain.SettingsRepo

class SettingsRepoImpl(private val settingsLocalSource: SettingsLocalSource) : SettingsRepo {
    override fun backupDatabase() = settingsLocalSource.backupDatabase()
    override fun restoreDatabase() = settingsLocalSource.restoreDatabase()
}
