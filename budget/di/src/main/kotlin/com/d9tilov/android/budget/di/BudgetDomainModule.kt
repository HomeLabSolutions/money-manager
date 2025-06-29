package com.d9tilov.android.budget.di

import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.budget.domain.impl.BudgetInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BudgetDomainModule {
    @Binds
    fun provideBudgetInteractor(impl: BudgetInteractorImpl): BudgetInteractor
}
