package com.d9tilov.moneymanager.prepopulate.di

import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.prepopulate.data.PrepopulateDataRepo
import com.d9tilov.moneymanager.prepopulate.domain.PrepopulateInteractor
import com.d9tilov.moneymanager.prepopulate.domain.PrepopulateInteractorImpl
import com.d9tilov.moneymanager.prepopulate.domain.PrepopulateRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class PrepopulateModule {

    @Provides
    @ActivityRetainedScoped
    fun providePrepopulateRepo(
        preferencesStore: PreferencesStore,
    ): PrepopulateRepo = PrepopulateDataRepo(preferencesStore)

    @Provides
    @ActivityRetainedScoped
    fun providePrepopulateInteractor(
        prepopulateRepo: PrepopulateRepo,
    ): PrepopulateInteractor = PrepopulateInteractorImpl(prepopulateRepo)
}
