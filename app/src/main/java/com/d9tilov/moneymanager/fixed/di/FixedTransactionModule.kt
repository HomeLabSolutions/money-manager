package com.d9tilov.moneymanager.fixed.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.fixed.data.FixedTransactionDataRepo
import com.d9tilov.moneymanager.fixed.data.local.FixedTransactionLocalSource
import com.d9tilov.moneymanager.fixed.data.local.FixedTransactionSource
import com.d9tilov.moneymanager.fixed.data.local.mapper.FixedTransactionDataMapper
import com.d9tilov.moneymanager.fixed.domain.FixedTransactionInteractor
import com.d9tilov.moneymanager.fixed.domain.FixedTransactionInteractorImpl
import com.d9tilov.moneymanager.fixed.domain.FixedTransactionRepo
import com.d9tilov.moneymanager.fixed.domain.mapper.FixedTransactionDomainMapper
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
        fixedTransactionDataMapper: FixedTransactionDataMapper,
    ): FixedTransactionSource =
        FixedTransactionLocalSource(preferencesStore, appDatabase, fixedTransactionDataMapper)

    @Provides
    @ActivityRetainedScoped
    fun provideFixedTransactionRepo(
        fixedTransactionSource: FixedTransactionSource,
    ): FixedTransactionRepo = FixedTransactionDataRepo(fixedTransactionSource)

    @Provides
    @ActivityRetainedScoped
    fun provideFixedTransactionInteractor(
        fixedTransactionRepo: FixedTransactionRepo,
        categoryInteractor: CategoryInteractor,
        fixedTransactionDomainMapper: FixedTransactionDomainMapper
    ): FixedTransactionInteractor = FixedTransactionInteractorImpl(
        fixedTransactionRepo,
        categoryInteractor,
        fixedTransactionDomainMapper
    )
}
