package com.d9tilov.moneymanager.settings.domain

interface ISettingsInteractor {

    fun saveNumber()
    fun restoreNumber(): Int
}
