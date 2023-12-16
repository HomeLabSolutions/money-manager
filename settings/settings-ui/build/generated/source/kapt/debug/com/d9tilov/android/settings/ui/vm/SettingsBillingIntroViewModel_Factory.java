package com.d9tilov.android.settings.ui.vm;

import com.d9tilov.android.billing.domain.contract.BillingInteractor;
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
public final class SettingsBillingIntroViewModel_Factory implements Factory<SettingsBillingIntroViewModel> {
  private final Provider<BillingInteractor> billingInteractorProvider;

  public SettingsBillingIntroViewModel_Factory(
      Provider<BillingInteractor> billingInteractorProvider) {
    this.billingInteractorProvider = billingInteractorProvider;
  }

  @Override
  public SettingsBillingIntroViewModel get() {
    return newInstance(billingInteractorProvider.get());
  }

  public static SettingsBillingIntroViewModel_Factory create(
      Provider<BillingInteractor> billingInteractorProvider) {
    return new SettingsBillingIntroViewModel_Factory(billingInteractorProvider);
  }

  public static SettingsBillingIntroViewModel newInstance(BillingInteractor billingInteractor) {
    return new SettingsBillingIntroViewModel(billingInteractor);
  }
}
