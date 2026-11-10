package com.d9tilov.android.budget.di

import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.budget.domain.impl.BudgetInteractorImpl
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val budgetDomainModule =
    module {
        factory<BudgetInteractorImpl>() bind BudgetInteractor::class
    }
