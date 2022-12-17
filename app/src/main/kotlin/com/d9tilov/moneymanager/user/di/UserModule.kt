package com.d9tilov.moneymanager.user.di

import com.d9tilov.android.database.AppDatabase
import com.d9tilov.android.datastore.PreferencesStore
import com.d9tilov.moneymanager.user.data.UserDataRepo
import com.d9tilov.android.database.dao.UserDao
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao = appDatabase.userDao()

    @Provides
    @Singleton
    fun provideUserLocalSource(
        preferenceStore: PreferencesStore,
        appDatabase: AppDatabase
    ): UserSource = UserLocalSource(preferenceStore, appDatabase.userDao())

    @Provides
    @Singleton
    fun userRepo(userLocalSource: UserSource): UserRepo = UserDataRepo(userLocalSource)

    @Provides
    @Singleton
    fun provideUserInfoInteractor(
        userRepo: UserRepo,
        userDomainMapper: UserDomainMapper
    ): UserInteractor =
        UserInfoInteractor(
            userRepo,
            userDomainMapper
        )
}
