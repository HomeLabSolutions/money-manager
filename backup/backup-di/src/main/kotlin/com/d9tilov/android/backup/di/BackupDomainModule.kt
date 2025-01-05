package com.d9tilov.android.backup.di

import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.backup.domain.contract.BackupRepo
import com.d9tilov.android.backup.domain.impl.BackupInteractorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackupDomainModule {
    @Provides
    @Singleton
    fun provideBackupInteractor(backupRepo: BackupRepo): BackupInteractor = BackupInteractorImpl(backupRepo)
}
