package com.d9tilov.android.goals.di

import com.d9tilov.android.currency.domain.contract.CurrencyInteractor
import com.d9tilov.android.goals.domain.contract.GoalInteractor
import com.d9tilov.android.goals.domain.contract.GoalRepo
import com.d9tilov.android.goals.domain.impl.GoalIteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object GoalsDomainModule {

    @Provides
    fun provideGoalInteractor(
        goalRepo: GoalRepo,
        currencyInteractor: CurrencyInteractor
    ): GoalInteractor = GoalIteractorImpl(goalRepo, currencyInteractor)
}
