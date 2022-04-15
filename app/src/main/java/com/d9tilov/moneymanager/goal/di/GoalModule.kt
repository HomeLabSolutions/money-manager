package com.d9tilov.moneymanager.goal.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.budget.domain.BudgetInteractor
import com.d9tilov.moneymanager.currency.domain.CurrencyInteractor
import com.d9tilov.moneymanager.goal.data.GoalRepoImpl
import com.d9tilov.moneymanager.goal.data.local.GoalLocalSource
import com.d9tilov.moneymanager.goal.data.local.GoalSource
import com.d9tilov.moneymanager.goal.domain.GoalInteractor
import com.d9tilov.moneymanager.goal.domain.GoalIteractorImpl
import com.d9tilov.moneymanager.goal.domain.GoalRepo
import com.d9tilov.moneymanager.goal.domain.mapper.GoalDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class GoalModule {

    @Provides
    @ActivityRetainedScoped
    fun provideGoalSource(
        preferencesStore: PreferencesStore,
        appDatabase: AppDatabase,
    ): GoalSource = GoalLocalSource(preferencesStore, appDatabase.goalDao())

    @Provides
    @ActivityRetainedScoped
    fun provideGoalRepo(goalSource: GoalSource): GoalRepo = GoalRepoImpl(goalSource)

    @Provides
    @ActivityRetainedScoped
    fun provideGoalInteractor(
        goalRepo: GoalRepo,
        budgetInteractor: BudgetInteractor,
        goalDomainMapper: GoalDomainMapper,
        currencyInteractor: CurrencyInteractor
    ): GoalInteractor =
        GoalIteractorImpl(goalRepo, budgetInteractor, goalDomainMapper, currencyInteractor)
}
