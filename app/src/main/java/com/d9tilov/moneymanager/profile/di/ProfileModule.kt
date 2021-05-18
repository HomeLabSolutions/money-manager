package com.d9tilov.moneymanager.profile.di

import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.profile.data.ProfileDataRepo
import com.d9tilov.moneymanager.profile.data.local.ProfileLocalSource
import com.d9tilov.moneymanager.profile.data.local.ProfileSource
import com.d9tilov.moneymanager.profile.domain.ProfileInteractor
import com.d9tilov.moneymanager.profile.domain.ProfileInteractorImpl
import com.d9tilov.moneymanager.profile.domain.ProfileRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class ProfileModule {

    @Provides
    @ActivityRetainedScoped
    fun profileLocalSource(preferencesStore: PreferencesStore): ProfileSource =
        ProfileLocalSource(preferencesStore)

    @Provides
    @ActivityRetainedScoped
    fun profileRepo(): ProfileRepo =
        ProfileDataRepo()

    @Provides
    @ActivityRetainedScoped
    fun provideProfileInteractor(profileRepo: ProfileRepo): ProfileInteractor =
        ProfileInteractorImpl(profileRepo)
}
