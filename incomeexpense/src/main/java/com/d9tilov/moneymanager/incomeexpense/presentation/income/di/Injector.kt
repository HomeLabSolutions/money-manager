package com.d9tilov.moneymanager.incomeexpense.presentation.income.di

import com.d9tilov.moneymanager.appComponent
import com.d9tilov.moneymanager.incomeexpense.presentation.income.ui.IncomeFragment

fun IncomeFragment.inject() {
    DaggerIncomeComponent.builder()
        .appComponent(appComponent())
        .incomeModule(IncomeModule(this))
        .build()
        .inject(this)
}
