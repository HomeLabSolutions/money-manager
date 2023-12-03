package com.d9tilov.android.backup.ui.vm;

import com.d9tilov.android.backup.domain.contract.BackupInteractor;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class BackupDeletionViewModel_Factory implements Factory<BackupDeletionViewModel> {
  private final Provider<BackupInteractor> backupInteractorProvider;

  public BackupDeletionViewModel_Factory(Provider<BackupInteractor> backupInteractorProvider) {
    this.backupInteractorProvider = backupInteractorProvider;
  }

  @Override
  public BackupDeletionViewModel get() {
    return newInstance(backupInteractorProvider.get());
  }

  public static BackupDeletionViewModel_Factory create(
      Provider<BackupInteractor> backupInteractorProvider) {
    return new BackupDeletionViewModel_Factory(backupInteractorProvider);
  }

  public static BackupDeletionViewModel newInstance(BackupInteractor backupInteractor) {
    return new BackupDeletionViewModel(backupInteractor);
  }
}
