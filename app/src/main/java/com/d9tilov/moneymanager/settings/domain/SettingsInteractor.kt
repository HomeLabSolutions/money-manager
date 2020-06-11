package com.d9tilov.moneymanager.settings.domain

interface SettingsInteractor {

    fun saveNumber()
    fun restoreNumber(): Int
}
