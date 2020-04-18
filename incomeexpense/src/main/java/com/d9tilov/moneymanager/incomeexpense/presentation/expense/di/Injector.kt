package com.d9tilov.moneymanager.incomeexpense.presentation.expense.di

import com.d9tilov.moneymanager.coreComponent
import com.d9tilov.moneymanager.incomeexpense.presentation.expense.ui.ExpenseFragment

fun ExpenseFragment.inject() {
    DaggerExpenseComponent.builder()
        .coreComponent(coreComponent())
        .expenseModule(ExpenseModule(this))
        .build()
        .inject(this)
}
