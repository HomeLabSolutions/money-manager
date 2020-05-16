package com.d9tilov.moneymanager.data.di

import com.d9tilov.moneymanager.data.base.local.db.AppDatabase
import com.d9tilov.moneymanager.data.base.local.db.prepopulate.PrepopulateDataManager
import com.d9tilov.moneymanager.data.base.local.preferences.PreferencesStore
import com.d9tilov.moneymanager.data.category.CategoryRepoImpl
import com.d9tilov.moneymanager.data.category.local.CategoryLocalSource
import com.d9tilov.moneymanager.data.category.local.CategoryLocalSourceImpl
import com.d9tilov.moneymanager.data.category.local.mappers.CategoryMapper
import com.d9tilov.moneymanager.data.user.UserRepoImpl
import com.d9tilov.moneymanager.data.user.local.UserLocalSource
import com.d9tilov.moneymanager.data.user.local.UserLocalSourceImpl
import com.d9tilov.moneymanager.data.user.local.mappers.DataUserMapper
import com.d9tilov.moneymanager.domain.category.CategoryRepo
import com.d9tilov.moneymanager.domain.user.UserInfoInteractor
import com.d9tilov.moneymanager.domain.user.UserInfoInteractorImpl
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

    @Provides
    fun provideUserInfoInteractor(
        userRepo: UserRepo,
        categoryRepo: CategoryRepo
    ): UserInfoInteractor = UserInfoInteractorImpl(userRepo,categoryRepo)

    @Provides
    fun provideCategoryLocalSource(
        categoryMapper: CategoryMapper,
        prepopulateDataManager: PrepopulateDataManager,
        database: AppDatabase
    ) : CategoryLocalSource = CategoryLocalSourceImpl(categoryMapper, prepopulateDataManager, database)

    @Provides
    fun categoryRepo(categoryLocalSource: CategoryLocalSource) :CategoryRepo = CategoryRepoImpl(categoryLocalSource)
}
