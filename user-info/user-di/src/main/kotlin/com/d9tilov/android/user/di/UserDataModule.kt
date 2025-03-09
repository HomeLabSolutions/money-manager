package com.d9tilov.android.user.di

import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.android.network.dispatchers.Dispatcher
import com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers
import com.d9tilov.android.user.data.contract.UserSource
import com.d9tilov.android.user.data.impl.UserDataRepo
import com.d9tilov.android.user.data.impl.UserLocalSource
import com.d9tilov.android.user.domain.contract.UserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object UserDataModule {
    @Provides
    fun provideUserSource(
        @Dispatcher(MoneyManagerDispatchers.IO) dispatcher: CoroutineDispatcher,
        appDatabase: AppDatabase,
        preferencesStore: PreferencesStore,
    ): UserSource = UserLocalSource(dispatcher, preferencesStore, appDatabase.userDao())

    @Provides
    fun provideUserRepo(userSource: UserSource): UserRepo = UserDataRepo(userSource)
}
