package com.d9tilov.moneymanager.periodic.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.periodic.data.PeriodicTransactionDataRepo
import com.d9tilov.moneymanager.periodic.data.local.PeriodicTransactionLocalSource
import com.d9tilov.moneymanager.periodic.data.local.PeriodicTransactionSource
import com.d9tilov.moneymanager.periodic.data.local.mapper.PeriodicTransactionDataMapper
import com.d9tilov.moneymanager.periodic.domain.PeriodicTransactionInteractor
import com.d9tilov.moneymanager.periodic.domain.PeriodicTransactionInteractorImpl
import com.d9tilov.moneymanager.periodic.domain.PeriodicTransactionRepo
import com.d9tilov.moneymanager.periodic.domain.mapper.PeriodicTransactionDomainMapper
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class FixedTransactionModule {

    @Provides
    @ActivityRetainedScoped
    fun provideFixedTransactionSource(
        preferencesStore: PreferencesStore,
        appDatabase: AppDatabase,
        periodicTransactionDataMapper: PeriodicTransactionDataMapper,
    ): PeriodicTransactionSource =
        PeriodicTransactionLocalSource(preferencesStore, appDatabase, periodicTransactionDataMapper)

    @Provides
    @ActivityRetainedScoped
    fun provideFixedTransactionRepo(
        periodicTransactionSource: PeriodicTransactionSource,
    ): PeriodicTransactionRepo = PeriodicTransactionDataRepo(periodicTransactionSource)

    @Provides
    @ActivityRetainedScoped
    fun provideFixedTransactionInteractor(
        periodicTransactionRepo: PeriodicTransactionRepo,
        categoryInteractor: CategoryInteractor,
        userInteractor: UserInteractor,
        periodicTransactionDomainMapper: PeriodicTransactionDomainMapper
    ): PeriodicTransactionInteractor = PeriodicTransactionInteractorImpl(
        periodicTransactionRepo,
        categoryInteractor,
        userInteractor,
        periodicTransactionDomainMapper
    )
}
