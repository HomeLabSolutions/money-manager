package com.d9tilov.moneymanager.user.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.category.domain.CategoryRepo
import com.d9tilov.moneymanager.user.data.UserDataRepo
import com.d9tilov.moneymanager.user.data.local.UserSource
import com.d9tilov.moneymanager.user.data.local.UserDao
import com.d9tilov.moneymanager.user.data.local.UserLocalSource
import com.d9tilov.moneymanager.user.data.local.mapper.DataUserMapper
import com.d9tilov.moneymanager.user.domain.UserInteractor
import com.d9tilov.moneymanager.user.domain.UserRepo
import com.d9tilov.moneymanager.user.domain.UserInfoInteractor
import com.d9tilov.moneymanager.user.domain.mapper.UserDomainMapper
import dagger.Module
import dagger.Provides

@Module
class UserModule {

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideUserLocalSource(
        preferenceStore: PreferencesStore,
        appDatabase: AppDatabase,
        dataUserMapper: DataUserMapper
    ): UserSource = UserLocalSource(preferenceStore, appDatabase, dataUserMapper)

    @Provides
    fun userRepo(
        userLocalSource: UserSource
    ): UserRepo = UserDataRepo(
        userLocalSource
    )

    @Provides
    fun provideUserInfoInteractor(
        userRepo: UserRepo,
        categoryRepo: CategoryRepo,
        userDomainMapper: UserDomainMapper
    ): UserInteractor =
        UserInfoInteractor(
            userRepo,
            categoryRepo,
            userDomainMapper
        )
}
