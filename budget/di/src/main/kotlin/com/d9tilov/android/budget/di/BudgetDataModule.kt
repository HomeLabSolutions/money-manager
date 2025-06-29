package com.d9tilov.android.budget.di

import com.d9tilov.android.budget.data.contract.BudgetSource
import com.d9tilov.android.budget.data.impl.BudgetDataRepo
import com.d9tilov.android.budget.data.impl.BudgetLocalSource
import com.d9tilov.android.budget.domain.contract.BudgetRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BudgetDataModule {
    @Binds
    fun provideBudgetLocalSource(impl: BudgetLocalSource): BudgetSource

    @Binds
    fun provideBudgetRepo(impl: BudgetDataRepo): BudgetRepo
}
