package com.d9tilov.android.backup.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\'J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\'J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\'\u00a8\u0006\u000e"}, d2 = {"Lcom/d9tilov/android/backup/di/BackupDataModule;", "", "provideBackupLocalSource", "Lcom/d9tilov/android/backup/data/contract/BackupSource;", "backupLocalSource", "Lcom/d9tilov/android/backup/data/impl/BackupLocalSource;", "provideBackupManager", "Lcom/d9tilov/android/backup/data/contract/BackupManager;", "backupManagerImpl", "Lcom/d9tilov/android/backup/data/impl/BackupManagerImpl;", "provideBackupRepo", "Lcom/d9tilov/android/backup/domain/contract/BackupRepo;", "backupDataRepo", "Lcom/d9tilov/android/backup/data/impl/BackupDataRepo;", "backup-di_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract interface BackupDataModule {
    
    @dagger.Binds
    @javax.inject.Singleton
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.backup.domain.contract.BackupRepo provideBackupRepo(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.backup.data.impl.BackupDataRepo backupDataRepo);
    
    @dagger.Binds
    @javax.inject.Singleton
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.backup.data.contract.BackupSource provideBackupLocalSource(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.backup.data.impl.BackupLocalSource backupLocalSource);
    
    @dagger.Binds
    @javax.inject.Singleton
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.backup.data.contract.BackupManager provideBackupManager(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.backup.data.impl.BackupManagerImpl backupManagerImpl);
}