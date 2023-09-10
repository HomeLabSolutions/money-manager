package com.d9tilov.android.goals.di

import com.d9tilov.android.goals.data.contract.GoalSource
import com.d9tilov.android.goals.data.impl.GoalLocalSource
import com.d9tilov.android.goals.data.impl.GoalRepoImpl
import com.d9tilov.android.goals.domain.contract.GoalRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
interface GoalsDataModule {

    @Binds
    fun provideGoalSource(goalLocalSource: GoalLocalSource): GoalSource

    @Binds
    fun provideGoalRepo(goalRepoImpl: GoalRepoImpl): GoalRepo
}
