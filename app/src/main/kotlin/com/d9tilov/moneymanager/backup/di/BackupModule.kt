package com.d9tilov.moneymanager.backup.di

import android.content.Context
import com.d9tilov.moneymanager.backup.BackupManager
import com.d9tilov.moneymanager.backup.data.BackupDataRepo
import com.d9tilov.moneymanager.backup.data.local.BackupLocalSource
import com.d9tilov.moneymanager.backup.data.local.BackupSource
import com.d9tilov.moneymanager.backup.domain.BackupInteractor
import com.d9tilov.moneymanager.backup.domain.BackupInteractorImpl
import com.d9tilov.moneymanager.backup.domain.BackupRepo
import com.d9tilov.android.datastore.PreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackupModule {

    @Provides
    @Singleton
    fun provideBackupInteractor(backupRepo: BackupRepo): BackupInteractor =
        BackupInteractorImpl(backupRepo)

    @Provides
    @Singleton
    fun provideBackupRepo(backupSource: BackupSource): BackupRepo = BackupDataRepo(backupSource)

    @Provides
    @Singleton
    fun provideBackupLocalSource(backupManager: BackupManager, preferencesStore: PreferencesStore): BackupSource =
        BackupLocalSource(backupManager, preferencesStore)

    @Provides
    @Singleton
    fun provideBackupManager(
        @ApplicationContext context: Context,
        preferencesStore: PreferencesStore
    ): BackupManager = BackupManager(context, preferencesStore)
}
