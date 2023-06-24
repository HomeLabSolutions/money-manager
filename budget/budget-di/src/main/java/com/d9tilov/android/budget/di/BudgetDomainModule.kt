package com.d9tilov.android.budget.di

import com.d9tilov.android.budget.domain.contract.BudgetRepo
import com.d9tilov.android.budget.domain.contract.BudgetInteractor
import com.d9tilov.android.budget.domain.impl.BudgetInteractorImpl
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BudgetDomainModule {

    @Provides
    fun provideBudgetInteractor(
        budgetRepo: BudgetRepo,
        currencyInteractor: CurrencyInteractor
    ): BudgetInteractor = BudgetInteractorImpl(budgetRepo, currencyInteractor)
}
