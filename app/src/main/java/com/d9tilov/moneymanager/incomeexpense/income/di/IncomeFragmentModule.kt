package com.d9tilov.moneymanager.incomeexpense.income.di

import androidx.lifecycle.ViewModel
import com.d9tilov.moneymanager.base.di.ViewModelKey
import com.d9tilov.moneymanager.incomeexpense.income.ui.IncomeViewModel
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class IncomeFragmentModule {

    @IntoMap
    @ViewModelKey(IncomeViewModel::class)
    @Provides
    fun provideIncomeViewModel(): ViewModel = IncomeViewModel()
}
