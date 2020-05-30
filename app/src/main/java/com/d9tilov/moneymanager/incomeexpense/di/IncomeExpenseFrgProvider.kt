package com.d9tilov.moneymanager.incomeexpense.di

import com.d9tilov.moneymanager.incomeexpense.expense.di.ExpenseFragmentModule
import com.d9tilov.moneymanager.incomeexpense.expense.ui.ExpenseFragment
import com.d9tilov.moneymanager.incomeexpense.income.di.IncomeFragmentModule
import com.d9tilov.moneymanager.incomeexpense.income.ui.IncomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class IncomeExpenseFrgProvider {

    @ContributesAndroidInjector(modules = [IncomeFragmentModule::class])
    abstract fun provideIncomeFragment(): IncomeFragment

    @ContributesAndroidInjector(modules = [ExpenseFragmentModule::class])
    abstract fun provideExpenseFragment(): ExpenseFragment
}
