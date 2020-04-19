package com.d9tilov.moneymanager.incomeexpense.presentation.expense.di

import com.d9tilov.moneymanager.appComponent
import com.d9tilov.moneymanager.incomeexpense.presentation.expense.ui.ExpenseFragment

fun ExpenseFragment.inject() {
    DaggerExpenseComponent.builder()
        .appComponent(appComponent())
        .expenseModule(ExpenseModule(this))
        .build()
        .inject(this)
}
