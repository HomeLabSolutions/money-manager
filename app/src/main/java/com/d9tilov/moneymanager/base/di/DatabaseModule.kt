package com.d9tilov.moneymanager.base.di

import android.content.Context
import com.d9tilov.moneymanager.base.data.local.db.AppDatabase
import com.d9tilov.moneymanager.user.data.local.UserDao
import dagger.Module
import dagger.Provides
import java.io.File

@Module
class DatabaseModule {

    @Provides
    fun provideAppDataBase(context: Context) = AppDatabase.getInstance(context)

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    fun provideDatabaseFile(context: Context): File =
        context.getDatabasePath(AppDatabase.DATABASE_NAME)
}
