package com.d9tilov.android.regular.transaction.di

import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.regular.transaction.data.contract.RegularTransactionSource
import com.d9tilov.android.regular.transaction.data.impl.RegularTransactionDataRepo
import com.d9tilov.android.regular.transaction.data.impl.RegularTransactionLocalSource
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RegularTransactionDataModule {

    @Provides
    fun provideRegularTransactionSource(
        preferencesStore: PreferencesStore,
        appDatabase: AppDatabase
    ): RegularTransactionSource =
        RegularTransactionLocalSource(preferencesStore, appDatabase.regularTransactionDao())

    @Provides
    fun provideRegularTransactionRepo(
        regularTransactionSource: RegularTransactionSource
    ): RegularTransactionRepo = RegularTransactionDataRepo(regularTransactionSource)
}
