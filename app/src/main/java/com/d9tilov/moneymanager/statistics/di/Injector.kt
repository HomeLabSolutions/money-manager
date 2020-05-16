package com.d9tilov.moneymanager.statistics.di

import com.d9tilov.moneymanager.appComponent
import com.d9tilov.moneymanager.statistics.ui.StatisticsFragment

fun StatisticsFragment.inject() {
    DaggerStatisticsComponent.builder()
        .appComponent(appComponent())
        .statisticsModule(StatisticsModule(this))
        .build()
        .inject(this)
}
