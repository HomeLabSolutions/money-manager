package com.d9tilov.moneymanager.settings.domain

interface SettingsRepo {

    fun saveNumber()

    fun restoreNumber():Int
}