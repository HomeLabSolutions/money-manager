package com.d9tilov.moneymanager.data.di

import android.content.Context
import com.d9tilov.moneymanager.backup.BackupManager
import com.d9tilov.moneymanager.backup.domain.BackupInteractor
import com.d9tilov.moneymanager.backup.domain.BackupInteractorImpl
import com.d9tilov.moneymanager.data.base.local.db.AppDatabase
import com.d9tilov.moneymanager.data.user.local.UserDao
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

    @Provides
    fun provideBackupInteractor(backupManager: BackupManager): BackupInteractor =
        BackupInteractorImpl(backupManager)
}
