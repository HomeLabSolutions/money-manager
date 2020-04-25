package com.d9tilov.moneymanager.data.di

import com.d9tilov.moneymanager.data.base.local.db.AppDatabase
import com.d9tilov.moneymanager.data.base.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.data.user.UserRepoImpl
import com.d9tilov.moneymanager.data.user.local.UserLocalSource
import com.d9tilov.moneymanager.data.user.local.UserLocalSourceImpl
import com.d9tilov.moneymanager.data.user.local.mappers.DataUserMapper
import com.d9tilov.moneymanager.domain.user.UserRepo
import dagger.Module
import dagger.Provides

@Module
class UserDataModule {

    @Provides
    fun provideUserLocalSource(
        userSharedPreferences: PreferencesStore,
        appDatabase: AppDatabase,
        dataUserMapper: DataUserMapper
    ): UserLocalSource = UserLocalSourceImpl(userSharedPreferences, appDatabase, dataUserMapper)

    @Provides
    fun userRepo(
        preferencesStore: PreferencesStore,
        userLocalSource: UserLocalSource
    ): UserRepo = UserRepoImpl(
        preferencesStore,
        userLocalSource
    )
}
