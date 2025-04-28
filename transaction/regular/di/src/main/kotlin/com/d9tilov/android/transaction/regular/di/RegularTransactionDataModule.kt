package com.d9tilov.android.transaction.regular.di

import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.dispatchers.Dispatcher
import com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers
import com.d9tilov.android.transaction.regular.data.contract.RegularTransactionSource
import com.d9tilov.android.transaction.regular.data.impl.RegularTransactionDataRepo
import com.d9tilov.android.transaction.regular.data.impl.RegularTransactionLocalSource
import com.d9tilov.android.transaction.regular.domain.contract.RegularTransactionRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ActivityRetainedComponent::class)
object RegularTransactionDataModule {
    @Provides
    fun provideRegularTransactionSource(
        @Dispatcher(MoneyManagerDispatchers.IO) dispatcher: CoroutineDispatcher,
        preferencesStore: PreferencesStore,
        appDatabase: AppDatabase,
    ): RegularTransactionSource =
        RegularTransactionLocalSource(dispatcher, preferencesStore, appDatabase.regularTransactionDao())

    @Provides
    fun provideRegularTransactionRepo(regularTransactionSource: RegularTransactionSource): RegularTransactionRepo =
        RegularTransactionDataRepo(regularTransactionSource)
}
