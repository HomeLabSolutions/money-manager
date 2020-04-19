package com.d9tilov.moneymanager.data.di

import android.content.Context
import com.d9tilov.moneymanager.data.base.local.db.AppDatabase
import com.d9tilov.moneymanager.data.user.local.UserDao
import dagger.Module
import dagger.Provides

@Module
class DataBaseModule {

    @Provides
    fun provideAppDataBase(context: Context) = AppDatabase.getInstance(context)

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }
}