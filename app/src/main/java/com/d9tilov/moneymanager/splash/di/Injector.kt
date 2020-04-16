package com.d9tilov.moneymanager.splash.di

import com.d9tilov.moneymanager.coreComponent
import com.d9tilov.moneymanager.splash.ui.SplashActivity

fun SplashActivity.inject() {
    DaggerSplashComponent.builder()
        .coreComponent(coreComponent())
        .splashModule(SplashModule(this))
        .build()
        .inject(this)
}
