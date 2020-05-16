package com.d9tilov.moneymanager.incomeexpense.provider

import com.d9tilov.moneymanager.incomeexpense.expense.ui.ExpenseFragment
import com.d9tilov.moneymanager.incomeexpense.income.ui.IncomeFragment
import com.d9tilov.moneymanager.incomeexpense.ui.IncomeExpenseFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class IncomeExpenseFrgProvider {

    @ContributesAndroidInjector
    abstract fun provideIncomeExpenseFragmentFactory():IncomeExpenseFragment
    @ContributesAndroidInjector
    abstract fun provideIncomeFragmentFactory(): IncomeFragment
    @ContributesAndroidInjector
    abstract fun provideExpenseFragmentFactory(): ExpenseFragment
}
