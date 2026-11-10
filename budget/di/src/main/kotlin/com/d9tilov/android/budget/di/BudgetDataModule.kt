package com.d9tilov.android.budget.di

import com.d9tilov.android.budget.data.contract.BudgetSource
import com.d9tilov.android.budget.data.impl.BudgetDataRepo
import com.d9tilov.android.budget.data.impl.BudgetLocalSource
import com.d9tilov.android.budget.domain.contract.BudgetRepo
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val budgetDataModule =
    module {
        factory<BudgetLocalSource>() bind BudgetSource::class
        factory<BudgetDataRepo>() bind BudgetRepo::class
    }
