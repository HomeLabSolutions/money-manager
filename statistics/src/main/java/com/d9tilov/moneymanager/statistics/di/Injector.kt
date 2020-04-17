package com.d9tilov.moneymanager.statistics.di

import com.d9tilov.moneymanager.coreComponent
import com.d9tilov.moneymanager.statistics.ui.StatisticsFragment

fun StatisticsFragment.inject() {
    DaggerStatisticsComponent.builder()
        .coreComponent(coreComponent())
        .statisticsModule(StatisticsModule(this))
        .build()
        .inject(this)
}
