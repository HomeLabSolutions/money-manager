package com.d9tilov.moneymanager.transaction.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.category.domain.CategoryInteractor
import com.d9tilov.moneymanager.transaction.data.TransactionDataRepo
import com.d9tilov.moneymanager.transaction.data.local.TransactionDao
import com.d9tilov.moneymanager.transaction.data.local.TransactionLocalSource
import com.d9tilov.moneymanager.transaction.data.local.TransactionSource
import com.d9tilov.moneymanager.transaction.data.mapper.TransactionDataMapper
import com.d9tilov.moneymanager.transaction.domain.TransactionInteractor
import com.d9tilov.moneymanager.transaction.domain.TransactionRepo
import com.d9tilov.moneymanager.transaction.domain.TransactionUserInteractor
import com.d9tilov.moneymanager.transaction.domain.mapper.TransactionDomainMapper
import dagger.Module
import dagger.Provides

@Module
class TransactionModule {

    @Provides
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao {
        return appDatabase.transactionDao()
    }

    @Provides
    fun provideTransactionLocalSource(
        preferencesStore: PreferencesStore,
        appDatabase: AppDatabase,
        mapper: TransactionDataMapper
    ): TransactionSource = TransactionLocalSource(preferencesStore, appDatabase, mapper)

    @Provides
    fun provideTransactionRepo(transactionSource: TransactionSource): TransactionRepo =
        TransactionDataRepo(transactionSource)

    @Provides
    fun provideTransactionInteractor(
        transactionRepo: TransactionRepo,
        categoryInteractor: CategoryInteractor,
        transactionDomainMapper: TransactionDomainMapper
    ): TransactionInteractor = TransactionUserInteractor(transactionRepo, categoryInteractor, transactionDomainMapper)
}
