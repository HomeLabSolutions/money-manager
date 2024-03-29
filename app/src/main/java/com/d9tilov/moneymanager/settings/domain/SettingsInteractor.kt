package com.d9tilov.moneymanager.settings.domain

class SettingsInteractor(private val settingsRepo: ISettingsRepo) : ISettingsInteractor {
    override fun saveNumber() {
        settingsRepo.saveNumber()
    }

    override fun restoreNumber() = settingsRepo.restoreNumber()
}
