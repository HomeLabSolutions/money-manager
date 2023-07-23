package com.d9tilov.android.budget.di

import com.d9tilov.android.budget.data.contract.BudgetSource
import com.d9tilov.android.budget.data.impl.BudgetDataRepo
import com.d9tilov.android.budget.data.impl.BudgetLocalSource
import com.d9tilov.android.budget.domain.contract.BudgetRepo
import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BudgetDataModule {

    @Provides
    @Singleton
    fun provideBudgetLocalSource(
        preferencesStore: PreferencesStore,
        database: AppDatabase
    ): BudgetSource =
        BudgetLocalSource(
            preferencesStore,
            database.budgetDao()
        )

    @Provides
    @Singleton
    fun provideBudgetRepo(budgetSource: BudgetSource): BudgetRepo = BudgetDataRepo(budgetSource)
}
