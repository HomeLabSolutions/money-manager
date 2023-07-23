package com.d9tilov.android.goals.di

import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.goals.data.contract.GoalSource
import com.d9tilov.android.goals.data.impl.GoalLocalSource
import com.d9tilov.android.goals.data.impl.GoalRepoImpl
import com.d9tilov.android.goals.domain.contract.GoalRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object GoalsDataModule {

    @Provides
    fun provideGoalSource(
        preferencesStore: PreferencesStore,
        appDatabase: AppDatabase
    ): GoalSource =
        GoalLocalSource(preferencesStore, appDatabase.goalDao())

    @Provides
    fun provideGoalRepo(goalSource: GoalSource): GoalRepo =
        GoalRepoImpl(goalSource)
}
