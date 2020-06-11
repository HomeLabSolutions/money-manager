package com.d9tilov.moneymanager.incomeexpense.expense.di

import androidx.lifecycle.ViewModel
import com.d9tilov.moneymanager.base.di.ViewModelKey
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.incomeexpense.expense.ui.ExpenseViewModel
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class ExpenseFragmentModule {

    @IntoMap
    @ViewModelKey(ExpenseViewModel::class)
    @Provides
    fun provideExpenseViewModel(
        categoryInteractor: CategoryInteractor,
        transactionInteractor: TransactionInteractor
    ): ViewModel =
        ExpenseViewModel(categoryInteractor, transactionInteractor)
}
