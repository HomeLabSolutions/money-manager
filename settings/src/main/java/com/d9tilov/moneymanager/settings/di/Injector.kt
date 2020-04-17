package com.d9tilov.moneymanager.settings.di

import com.d9tilov.moneymanager.coreComponent
import com.d9tilov.moneymanager.settings.ui.SettingsFragment

fun SettingsFragment.inject() {
    DaggerSettingsComponent.builder()
        .coreComponent(coreComponent())
        .settingsModule(SettingsModule(this))
        .build()
        .inject(this)
}
