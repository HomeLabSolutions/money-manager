package com.d9tilov.moneymanager.transaction.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.transaction.data.TransactionDataRepo
import com.d9tilov.moneymanager.transaction.data.local.TransactionDao
import com.d9tilov.moneymanager.transaction.data.local.TransactionLocalSource
import com.d9tilov.moneymanager.transaction.data.local.TransactionSource
import com.d9tilov.moneymanager.transaction.data.mapper.TransactionDataMapper
import com.d9tilov.moneymanager.transaction.data.mapper.TransactionDateDataMapper
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.TransactionRepo
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractorImpl
import com.d9tilov.moneymanager.transaction.domain.mapper.TransactionDomainMapper
import com.d9tilov.moneymanager.transaction.domain.mapper.TransactionHeaderDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class TransactionModule {

    @Provides
    @ActivityRetainedScoped
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao {
        return appDatabase.transactionDao()
    }

    @Provides
    @ActivityRetainedScoped
    fun provideTransactionLocalSource(
        preferencesStore: PreferencesStore,
        appDatabase: AppDatabase,
        transactionDataMapper: TransactionDataMapper,
        transactionDateDataMapper: TransactionDateDataMapper
    ): TransactionSource = TransactionLocalSource(
        preferencesStore,
        appDatabase,
        transactionDataMapper,
        transactionDateDataMapper
    )

    @Provides
    fun provideTransactionRepo(transactionSource: TransactionSource): TransactionRepo =
        TransactionDataRepo(transactionSource)

    @Provides
    fun provideTransactionInteractor(
        transactionRepo: TransactionRepo,
        categoryInteractor: CategoryInteractor,
        transactionDomainMapper: TransactionDomainMapper,
        transactionHeaderDomainMapper: TransactionHeaderDomainMapper
    ): TransactionInteractor = TransactionInteractorImpl(
        transactionRepo,
        categoryInteractor,
        transactionDomainMapper,
        transactionHeaderDomainMapper
    )
}
