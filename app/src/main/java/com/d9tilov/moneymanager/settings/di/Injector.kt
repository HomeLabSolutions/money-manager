package com.d9tilov.moneymanager.settings.di

import com.d9tilov.moneymanager.appComponent
import com.d9tilov.moneymanager.settings.ui.SettingsFragment

fun SettingsFragment.inject() {
    DaggerSettingsComponent.builder()
        .appComponent(appComponent())
        .settingsModule(SettingsModule(this))
        .build()
        .inject(this)
}
