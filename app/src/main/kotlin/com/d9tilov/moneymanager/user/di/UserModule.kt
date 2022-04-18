package com.d9tilov.moneymanager.user.di

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
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class UserModule {

    @Provides
    @ActivityRetainedScoped
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @ActivityRetainedScoped
    fun provideUserLocalSource(
        preferenceStore: PreferencesStore,
        appDatabase: AppDatabase
    ): UserSource {
        return UserLocalSource(preferenceStore, appDatabase.userDao())
    }

    @Provides
    @ActivityRetainedScoped
    fun userRepo(userLocalSource: UserSource): UserRepo = UserDataRepo(userLocalSource)

    @Provides
    @ActivityRetainedScoped
    fun provideUserInfoInteractor(
        userRepo: UserRepo,
        userDomainMapper: UserDomainMapper
    ): UserInteractor =
        UserInfoInteractor(
            userRepo,
            userDomainMapper
        )
}
