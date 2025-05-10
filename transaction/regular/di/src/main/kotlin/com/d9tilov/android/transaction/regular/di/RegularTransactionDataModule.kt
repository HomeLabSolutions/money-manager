package com.d9tilov.android.transaction.regular.di

import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.transaction.regular.data.contract.RegularTransactionSource
import com.d9tilov.android.transaction.regular.data.impl.RegularTransactionDataRepo
import com.d9tilov.android.transaction.regular.data.impl.RegularTransactionLocalSource
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionRepo
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
        appDatabase: AppDatabase,
    ): RegularTransactionSource = RegularTransactionLocalSource(preferencesStore, appDatabase.regularTransactionDao())

    @Provides
    fun provideRegularTransactionRepo(regularTransactionSource: RegularTransactionSource): RegularTransactionRepo =
        RegularTransactionDataRepo(regularTransactionSource)
}
