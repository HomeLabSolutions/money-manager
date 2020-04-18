package com.d9tilov.moneymanager.incomeexpense.presentation.income.di

import com.d9tilov.moneymanager.coreComponent
import com.d9tilov.moneymanager.incomeexpense.presentation.income.ui.IncomeFragment

fun IncomeFragment.inject() {
    DaggerIncomeComponent.builder()
        .coreComponent(coreComponent())
        .incomeModule(IncomeModule(this))
        .build()
        .inject(this)
}
