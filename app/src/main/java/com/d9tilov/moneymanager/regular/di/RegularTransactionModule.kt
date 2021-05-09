package com.d9tilov.moneymanager.regular.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.regular.data.RegularTransactionDataRepo
import com.d9tilov.moneymanager.regular.data.local.RegularTransactionLocalSource
import com.d9tilov.moneymanager.regular.data.local.RegularTransactionSource
import com.d9tilov.moneymanager.regular.data.local.mapper.RegularTransactionDataMapper
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractor
import com.d9tilov.moneymanager.regular.domain.RegularTransactionInteractorImpl
import com.d9tilov.moneymanager.regular.domain.RegularTransactionRepo
import com.d9tilov.moneymanager.regular.domain.mapper.RegularTransactionDomainMapper
import com.d9tilov.moneymanager.user.domain.UserInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class RegularTransactionModule {

    @Provides
    @ActivityRetainedScoped
    fun provideRegularTransactionSource(
        preferencesStore: PreferencesStore,
        appDatabase: AppDatabase,
        regularTransactionDataMapper: RegularTransactionDataMapper,
    ): RegularTransactionSource =
        RegularTransactionLocalSource(preferencesStore, appDatabase, regularTransactionDataMapper)

    @Provides
    @ActivityRetainedScoped
    fun provideRegularTransactionRepo(
        regularTransactionSource: RegularTransactionSource,
    ): RegularTransactionRepo = RegularTransactionDataRepo(regularTransactionSource)

    @Provides
    @ActivityRetainedScoped
    fun provideRegularTransactionInteractor(
        regularTransactionRepo: RegularTransactionRepo,
        categoryInteractor: CategoryInteractor,
        userInteractor: UserInteractor,
        regularTransactionDomainMapper: RegularTransactionDomainMapper
    ): RegularTransactionInteractor = RegularTransactionInteractorImpl(
        regularTransactionRepo,
        categoryInteractor,
        userInteractor,
        regularTransactionDomainMapper
    )
}
