package com.d9tilov.android.backup.di

import com.d9tilov.android.backup.domain.contract.BackupInteractor
import com.d9tilov.android.backup.domain.impl.BackupInteractorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface BackupDomainModule {
    @Binds
    fun provideBackupInteractor(impl: BackupInteractorImpl): BackupInteractor
}
