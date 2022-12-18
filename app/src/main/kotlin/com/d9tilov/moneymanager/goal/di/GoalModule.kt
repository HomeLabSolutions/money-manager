package com.d9tilov.moneymanager.goal.di

import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.budget.domain.contract.BudgetInteractor
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

@Module
@InstallIn(ActivityRetainedComponent::class)
object GoalModule {

    @Provides
    fun provideGoalSource(
        preferencesStore: PreferencesStore,
        appDatabase: AppDatabase
    ): GoalSource = GoalLocalSource(preferencesStore, appDatabase.goalDao())

    @Provides
    fun provideGoalRepo(goalSource: GoalSource): GoalRepo = GoalRepoImpl(goalSource)

    @Provides
    fun provideGoalInteractor(
        goalRepo: GoalRepo,
        currencyInteractor: com.d9tilov.android.currency.domain.contract.CurrencyInteractor,
        budgetInteractor: com.d9tilov.android.budget.domain.contract.BudgetInteractor,
        goalDomainMapper: GoalDomainMapper
    ): GoalInteractor =
        GoalIteractorImpl(goalRepo, currencyInteractor, budgetInteractor, goalDomainMapper)
}
