package com.d9tilov.moneymanager.statistics.di.provider

import com.d9tilov.moneymanager.statistics.ui.StatisticsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class StatisticsFrgProvider {

    @ContributesAndroidInjector
    abstract fun provideStatisticsFragment(): StatisticsFragment
}
