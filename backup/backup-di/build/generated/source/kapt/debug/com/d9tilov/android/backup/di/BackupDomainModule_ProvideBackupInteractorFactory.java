package com.d9tilov.android.backup.di;

import com.d9tilov.android.backup.domain.contract.BackupInteractor;
import com.d9tilov.android.backup.domain.contract.BackupRepo;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class BackupDomainModule_ProvideBackupInteractorFactory implements Factory<BackupInteractor> {
  private final Provider<BackupRepo> backupRepoProvider;

  public BackupDomainModule_ProvideBackupInteractorFactory(
      Provider<BackupRepo> backupRepoProvider) {
    this.backupRepoProvider = backupRepoProvider;
  }

  @Override
  public BackupInteractor get() {
    return provideBackupInteractor(backupRepoProvider.get());
  }

  public static BackupDomainModule_ProvideBackupInteractorFactory create(
      Provider<BackupRepo> backupRepoProvider) {
    return new BackupDomainModule_ProvideBackupInteractorFactory(backupRepoProvider);
  }

  public static BackupInteractor provideBackupInteractor(BackupRepo backupRepo) {
    return Preconditions.checkNotNullFromProvides(BackupDomainModule.INSTANCE.provideBackupInteractor(backupRepo));
  }
}
