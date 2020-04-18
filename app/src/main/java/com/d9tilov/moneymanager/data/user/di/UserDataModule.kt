package com.d9tilov.moneymanager.data.user.di

import com.d9tilov.moneymanager.data.base.local.db.AppDatabase
import com.d9tilov.moneymanager.data.base.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.data.user.UserRepoImpl
import com.d9tilov.moneymanager.data.user.local.UserDao
import com.d9tilov.moneymanager.data.user.local.UserLocalSource
import com.d9tilov.moneymanager.data.user.local.UserLocalSourceImpl
import com.d9tilov.moneymanager.data.user.local.mappers.UserMapper
import com.d9tilov.moneymanager.domain.user.UserRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UserDataModule {

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }

    @Provides
    fun provideUserLocalSource(
        userSharedPreferences: PreferencesStore,
        appDatabase: AppDatabase,
        userMapper: UserMapper
    ): UserLocalSource = UserLocalSourceImpl(userSharedPreferences, appDatabase, userMapper)

    @Provides
    fun userRepo(
        preferencesStore: PreferencesStore,
        userLocalSource: UserLocalSource
    ): UserRepo = UserRepoImpl(
        preferencesStore,
        userLocalSource
    )
}
