package com.d9tilov.android.budget.di

import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.budget.domain.impl.BudgetInteractorImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface BudgetDomainModule {
    @Binds
    val BudgetInteractorImpl.provideBudgetInteractor: BudgetInteractor
}
