package com.d9tilov.moneymanager.statistics.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.d9tilov.moneymanager.statistics.ui.StatisticsFragment
import com.d9tilov.moneymanager.statistics.vm.StatisticsViewModel
import com.d9tilov.moneymanager.statistics.vm.StatisticsViewModelFactory
import dagger.Module
import dagger.Provides


@Module
class StatisticsModule(private val fragment: StatisticsFragment) {

    @Provides
    fun provideContext(): Context = fragment.requireContext()

    @Provides
    fun provideStatisticsViewModel(factory: StatisticsViewModelFactory): StatisticsViewModel {
        return ViewModelProvider(fragment, factory).get(StatisticsViewModel::class.java)
    }

    @Provides
    fun statisticsViewModelFactory() = StatisticsViewModelFactory()
}
