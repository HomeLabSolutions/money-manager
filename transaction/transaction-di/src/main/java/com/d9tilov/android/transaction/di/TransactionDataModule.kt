package com.d9tilov.android.transaction.di

import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.database.dao.TransactionDao
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.transaction.data.contract.TransactionSource
import com.d9tilov.android.transaction.data.impl.TransactionDataRepo
import com.d9tilov.android.transaction.data.impl.TransactionLocalSource
import com.d9tilov.android.transaction.domain.contract.TransactionRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object TransactionDataModule {

    @Provides
    @ActivityRetainedScoped
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao {
        return appDatabase.transactionDao()
    }

    @Provides
    @ActivityRetainedScoped
    fun provideTransactionLocalSource(
        preferencesStore: PreferencesStore,
        appDatabase: AppDatabase
    ): TransactionSource = TransactionLocalSource(preferencesStore, appDatabase.transactionDao())

    @Provides
    @ActivityRetainedScoped
    fun provideTransactionRepo(transactionSource: TransactionSource): TransactionRepo =
        TransactionDataRepo(transactionSource)
}
