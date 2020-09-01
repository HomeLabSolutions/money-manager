package com.d9tilov.moneymanager.budget.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.budget.data.BudgetDataRepo
import com.d9tilov.moneymanager.budget.data.local.BudgetLocalSource
import com.d9tilov.moneymanager.budget.data.local.BudgetSource
import com.d9tilov.moneymanager.budget.data.local.mapper.BudgetMapper
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.budget.domain.BudgetInteractorImpl
import com.d9tilov.moneymanager.budget.domain.BudgetRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class BudgetModule {

    @Provides
    fun provideBudgetInteractor(budgetRepo: BudgetRepo): BudgetInteractor =
        BudgetInteractorImpl(budgetRepo)

    @Provides
    @ActivityRetainedScoped
    fun provideBudgetLocalSource(
        preferencesStore: PreferencesStore,
        database: AppDatabase,
        budgetMapper: BudgetMapper
    ): BudgetSource = BudgetLocalSource(preferencesStore, database, budgetMapper)

    @Provides
    fun provideBudgetRepo(budgetSource: BudgetSource): BudgetRepo =
        BudgetDataRepo(budgetSource)
}
