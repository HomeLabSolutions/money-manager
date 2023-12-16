package com.d9tilov.android.settings.ui.vm;

import com.d9tilov.android.backup.domain.contract.BackupInteractor;
import com.d9tilov.android.billing.domain.contract.BillingInteractor;
import com.d9tilov.android.user.domain.contract.UserInteractor;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<BackupInteractor> backupInteractorProvider;

  private final Provider<UserInteractor> userInteractorProvider;

  private final Provider<BillingInteractor> billingInteractorProvider;

  public SettingsViewModel_Factory(Provider<BackupInteractor> backupInteractorProvider,
      Provider<UserInteractor> userInteractorProvider,
      Provider<BillingInteractor> billingInteractorProvider) {
    this.backupInteractorProvider = backupInteractorProvider;
    this.userInteractorProvider = userInteractorProvider;
    this.billingInteractorProvider = billingInteractorProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(backupInteractorProvider.get(), userInteractorProvider.get(), billingInteractorProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<BackupInteractor> backupInteractorProvider,
      Provider<UserInteractor> userInteractorProvider,
      Provider<BillingInteractor> billingInteractorProvider) {
    return new SettingsViewModel_Factory(backupInteractorProvider, userInteractorProvider, billingInteractorProvider);
  }

  public static SettingsViewModel newInstance(BackupInteractor backupInteractor,
      UserInteractor userInteractor, BillingInteractor billingInteractor) {
    return new SettingsViewModel(backupInteractor, userInteractor, billingInteractor);
  }
}
