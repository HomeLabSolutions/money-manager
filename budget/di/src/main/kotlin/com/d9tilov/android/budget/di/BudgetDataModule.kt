package com.d9tilov.android.budget.di

import com.d9tilov.android.budget.data.contract.BudgetSource
import com.d9tilov.android.budget.data.impl.BudgetDataRepo
import com.d9tilov.android.budget.data.impl.BudgetLocalSource
import com.d9tilov.android.budget.domain.contract.BudgetRepo
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(AppScope::class)
interface BudgetDataModule {
    @Binds
    val BudgetLocalSource.provideBudgetLocalSource: BudgetSource

    @Binds
    val BudgetDataRepo.provideBudgetRepo: BudgetRepo
}
