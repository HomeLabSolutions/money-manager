package com.d9tilov.moneymanager.incomeexpense.expense.di

import com.d9tilov.moneymanager.appComponent
import com.d9tilov.moneymanager.incomeexpense.expense.ui.ExpenseFragment
import com.d9tilov.moneymanager.settings.di.SettingsModule

fun ExpenseFragment.inject() {
    DaggerExpenseComponent.builder()
        .appComponent(appComponent())
        .expenseModule(ExpenseModule(this))
        .build()
        .inject(this)
}
