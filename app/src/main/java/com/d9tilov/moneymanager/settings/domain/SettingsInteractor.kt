package com.d9tilov.moneymanager.settings.domain

import com.d9tilov.moneymanager.base.domain.Interactor

interface SettingsInteractor : Interactor {

    fun saveNumber()
    fun restoreNumber(): Int
}
