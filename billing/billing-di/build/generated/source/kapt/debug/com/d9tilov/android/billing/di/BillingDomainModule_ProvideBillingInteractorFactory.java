package com.d9tilov.android.billing.di;

import com.d9tilov.android.billing.domain.contract.BillingInteractor;
import com.d9tilov.android.billing.domain.contract.BillingRepo;
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
public final class BillingDomainModule_ProvideBillingInteractorFactory implements Factory<BillingInteractor> {
  private final Provider<BillingRepo> billingRepoProvider;

  public BillingDomainModule_ProvideBillingInteractorFactory(
      Provider<BillingRepo> billingRepoProvider) {
    this.billingRepoProvider = billingRepoProvider;
  }

  @Override
  public BillingInteractor get() {
    return provideBillingInteractor(billingRepoProvider.get());
  }

  public static BillingDomainModule_ProvideBillingInteractorFactory create(
      Provider<BillingRepo> billingRepoProvider) {
    return new BillingDomainModule_ProvideBillingInteractorFactory(billingRepoProvider);
  }

  public static BillingInteractor provideBillingInteractor(BillingRepo billingRepo) {
    return Preconditions.checkNotNullFromProvides(BillingDomainModule.INSTANCE.provideBillingInteractor(billingRepo));
  }
}
