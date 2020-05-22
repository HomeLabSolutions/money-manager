package com.d9tilov.moneymanager.settings.domain

interface ISettingsRepo {

    fun saveNumber()

    fun restoreNumber(): Int
}
