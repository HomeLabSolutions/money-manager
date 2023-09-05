package com.d9tilov.android.backup.di

import com.d9tilov.android.backup.data.contract.BackupManager
import com.d9tilov.android.backup.data.contract.BackupSource
import com.d9tilov.android.backup.data.impl.BackupDataRepo
import com.d9tilov.android.backup.data.impl.BackupLocalSource
import com.d9tilov.android.backup.data.impl.BackupManagerImpl
import com.d9tilov.android.backup.domain.contract.BackupRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BackupDataModule {

    @Binds
    @Singleton
    fun provideBackupRepo(backupDataRepo: BackupDataRepo): BackupRepo

    @Binds
    @Singleton
    fun provideBackupLocalSource(backupLocalSource: BackupLocalSource): BackupSource

    @Binds
    @Singleton
    fun provideBackupManager(backupManagerImpl: BackupManagerImpl): BackupManager
}
