package com.d9tilov.android.backup.di

import android.content.Context
import com.d9tilov.android.backup.data.contract.BackupManager
import com.d9tilov.android.backup.data.contract.BackupRepo
import com.d9tilov.android.backup.data.contract.BackupSource
import com.d9tilov.android.backup.data.impl.BackupDataRepo
import com.d9tilov.android.backup.data.impl.BackupLocalSource
import com.d9tilov.android.backup.data.impl.BackupManagerImpl
import com.d9tilov.android.datastore.PreferencesStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackupDataModule {

    @Provides
    @Singleton
    fun provideBackupRepo(backupSource: BackupSource): BackupRepo =
        BackupDataRepo(backupSource)

    @Provides
    @Singleton
    fun provideBackupLocalSource(backupManager: BackupManager, preferencesStore: PreferencesStore): BackupSource =
        BackupLocalSource(backupManager, preferencesStore)

    @Provides
    @Singleton
    fun provideBackupManager(
        @ApplicationContext context: Context,
        preferencesStore: PreferencesStore
    ): BackupManager = BackupManagerImpl(context, preferencesStore)
}
