package com.d9tilov.moneymanager.incomeexpense.income.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.d9tilov.moneymanager.incomeexpense.income.ui.IncomeFragment
import com.d9tilov.moneymanager.incomeexpense.income.ui.IncomeViewModel
import com.d9tilov.moneymanager.incomeexpense.income.ui.IncomeViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class IncomeModule(private val fragment: IncomeFragment) {

    @Provides
    fun provideContext(): Context = fragment.requireContext()

    @Provides
    fun provideIncomeViewModel(factory: IncomeViewModelFactory): IncomeViewModel {
        return ViewModelProvider(fragment, factory).get(IncomeViewModel::class.java)
    }

    @Provides
    fun incomeViewModelFactory() = IncomeViewModelFactory()
}
