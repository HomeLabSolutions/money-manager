package com.d9tilov.moneymanager.user.di

import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.base.data.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.category.ICategoryRepo
import com.d9tilov.moneymanager.user.data.UserRepo
import com.d9tilov.moneymanager.user.data.local.IUserLocalSource
import com.d9tilov.moneymanager.user.data.local.UserLocalSource
import com.d9tilov.moneymanager.user.data.local.mappers.DataUserMapper
import com.d9tilov.moneymanager.user.domain.IUserInfoInteractor
import com.d9tilov.moneymanager.user.domain.IUserRepo
import com.d9tilov.moneymanager.user.domain.UserInfoInteractor
import com.d9tilov.moneymanager.user.mapper.DomainUserMapper
import dagger.Module
import dagger.Provides

@Module
class UserModule {

    @Provides
    fun provideUserLocalSource(
        userSharedPreferences: PreferencesStore,
        appDatabase: AppDatabase,
        dataUserMapper: DataUserMapper
    ): IUserLocalSource = UserLocalSource(userSharedPreferences, appDatabase, dataUserMapper)

    @Provides
    fun userRepo(
        preferencesStore: PreferencesStore,
        userLocalSource: IUserLocalSource
    ): IUserRepo = UserRepo(
        preferencesStore,
        userLocalSource
    )

    @Provides
    fun provideUserInfoInteractor(
        userRepo: IUserRepo,
        categoryRepo: ICategoryRepo,
        userMapper: DomainUserMapper
    ): IUserInfoInteractor =
        UserInfoInteractor(
            userRepo,
            categoryRepo,
            userMapper
        )
}
