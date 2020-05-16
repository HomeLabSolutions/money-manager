package com.d9tilov.moneymanager.home.di

import com.d9tilov.moneymanager.appComponent
import com.d9tilov.moneymanager.home.HomeActivity

fun HomeActivity.inject() {
    DaggerHomeComponent.builder()
        .appComponent(appComponent())
        .build()
        .inject(this)
}
