package com.d9tilov.moneymanager.base.di

import androidx.lifecycle.ViewModel
import com.d9tilov.moneymanager.category.ui.vm.CategoryViewModel
import com.d9tilov.moneymanager.home.ui.vm.HomeViewModel
import com.d9tilov.moneymanager.incomeexpense.expense.ui.ExpenseViewModel
import com.d9tilov.moneymanager.incomeexpense.income.ui.IncomeViewModel
import com.d9tilov.moneymanager.settings.ui.vm.SettingsViewModel
import com.d9tilov.moneymanager.splash.vm.SplashViewModel
import com.d9tilov.moneymanager.statistics.vm.StatisticsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomeViewModel::class)
    abstract fun bindIncomeViewModel(viewModel: IncomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpenseViewModel::class)
    abstract fun bindExpenseViewModel(viewModel: ExpenseViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StatisticsViewModel::class)
    abstract fun bindStatisticsViewModel(viewModel: StatisticsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategoryViewModel(viewModel: CategoryViewModel): ViewModel
}
