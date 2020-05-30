package com.d9tilov.moneymanager.statistics.di

import androidx.lifecycle.ViewModel
import com.d9tilov.moneymanager.base.di.ViewModelKey
import com.d9tilov.moneymanager.statistics.vm.StatisticsViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class StatisticsFragmentModule {

    @Provides
    @IntoMap
    @ViewModelKey(StatisticsViewModel::class)
    fun provideStatisticsViewModel(): ViewModel = StatisticsViewModel()
}
