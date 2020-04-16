package com.d9tilov.moneymanager.incomeexpense.expense.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.d9tilov.moneymanager.incomeexpense.expense.ui.ExpenseFragment
import com.d9tilov.moneymanager.incomeexpense.expense.ui.ExpenseViewModel
import com.d9tilov.moneymanager.incomeexpense.expense.ui.ExpenseViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ExpenseModule(private val fragment: ExpenseFragment) {

    @Provides
    fun provideContext(): Context = fragment.requireContext()

    @Provides
    fun provideIncomeViewModel(factory: ExpenseViewModelFactory): ExpenseViewModel {
        return ViewModelProvider(fragment, factory).get(ExpenseViewModel::class.java)
    }

    @Provides
    fun expenseViewModelFactory() = ExpenseViewModelFactory()

}
