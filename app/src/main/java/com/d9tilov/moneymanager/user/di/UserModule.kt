package com.d9tilov.moneymanager.user.di

import android.content.Context
import com.d9tilov.moneymanager.backup.BackupManager
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.user.data.UserDataRepo
import com.d9tilov.moneymanager.user.data.local.UserDao
import com.d9tilov.moneymanager.user.data.local.UserLocalSource
import com.d9tilov.moneymanager.user.data.local.UserSource
import com.d9tilov.moneymanager.user.domain.UserInfoInteractor
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.d9tilov.moneymanager.user.domain.UserRepo
import com.d9tilov.moneymanager.user.domain.mapper.UserDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UserModule {

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideUserLocalSource(
        context: Context,
        preferenceStore: PreferencesStore,
        appDatabase: AppDatabase,
        backupManager: BackupManager
    ): UserSource =
        UserLocalSource(context, preferenceStore, appDatabase.userDao(), backupManager)

    @Provides
    fun userRepo(
        userLocalSource: UserSource
    ): UserRepo = UserDataRepo(
        userLocalSource
    )

    @Provides
    fun provideUserInfoInteractor(
        userRepo: UserRepo,
        userDomainMapper: UserDomainMapper
    ): UserInteractor =
        UserInfoInteractor(
            userRepo,
            userDomainMapper
        )
}
