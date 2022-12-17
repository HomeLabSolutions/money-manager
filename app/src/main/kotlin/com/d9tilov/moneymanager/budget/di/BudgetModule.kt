package com.d9tilov.moneymanager.budget.di

import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.moneymanager.budget.data.BudgetDataRepo
import com.d9tilov.moneymanager.budget.data.local.BudgetLocalSource
import com.d9tilov.moneymanager.budget.data.local.BudgetSource
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.budget.domain.BudgetInteractorImpl
import com.d9tilov.moneymanager.budget.domain.BudgetRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BudgetModule {

    @Provides
    @Singleton
    fun provideBudgetInteractor(
        budgetRepo: BudgetRepo,
        currencyInteractor: com.d9tilov.android.currency.domain.contract.CurrencyInteractor
    ): BudgetInteractor =
        BudgetInteractorImpl(budgetRepo, currencyInteractor)

    @Provides
    @Singleton
    fun provideBudgetLocalSource(
        preferencesStore: PreferencesStore,
        database: AppDatabase
    ): BudgetSource = BudgetLocalSource(preferencesStore, database.budgetDao())

    @Provides
    @Singleton
    fun provideBudgetRepo(budgetSource: BudgetSource): BudgetRepo =
        BudgetDataRepo(budgetSource)
}
