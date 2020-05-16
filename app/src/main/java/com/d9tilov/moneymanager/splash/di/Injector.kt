package com.d9tilov.moneymanager.splash.di

import com.d9tilov.moneymanager.appComponent
import com.d9tilov.moneymanager.data.di.DatabaseModule
import com.d9tilov.moneymanager.data.di.UserDataModule
import com.d9tilov.moneymanager.splash.ui.SplashActivity

fun SplashActivity.inject() {
    DaggerSplashComponent.builder()
        .appComponent(appComponent())
        .splashModule(SplashModule(this))
        .userModule(UserDataModule())
        .databaseModule(DatabaseModule())
        .build()
        .inject(this)
}
