package com.d9tilov.moneymanager.presentation.splash.di

import com.d9tilov.moneymanager.appComponent
import com.d9tilov.moneymanager.presentation.splash.ui.SplashActivity

fun SplashActivity.inject() {
    DaggerSplashComponent.builder()
        .appComponent(appComponent())
        .splashModule(SplashModule(this))
        .build()
        .inject(this)
}
