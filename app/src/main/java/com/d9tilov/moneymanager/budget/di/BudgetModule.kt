package com.d9tilov.moneymanager.budget.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.budget.data.BudgetDataRepo
import com.d9tilov.moneymanager.budget.data.local.BudgetLocalSource
import com.d9tilov.moneymanager.budget.data.local.BudgetSource
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.budget.domain.BudgetInteractorImpl
import com.d9tilov.moneymanager.budget.domain.BudgetRepo
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
class BudgetModule {

    @Provides
    fun provideBudgetInteractor(
        budgetRepo: BudgetRepo,
        userInteractor: UserInteractor,
        currencyInteractor: CurrencyInteractor
    ): BudgetInteractor =
        BudgetInteractorImpl(budgetRepo, userInteractor, currencyInteractor)

    @Provides
    fun provideBudgetLocalSource(
        preferencesStore: PreferencesStore,
        database: AppDatabase
    ): BudgetSource = BudgetLocalSource(preferencesStore, database.budgetDao())

    @Provides
    fun provideBudgetRepo(budgetSource: BudgetSource): BudgetRepo =
        BudgetDataRepo(budgetSource)
}
