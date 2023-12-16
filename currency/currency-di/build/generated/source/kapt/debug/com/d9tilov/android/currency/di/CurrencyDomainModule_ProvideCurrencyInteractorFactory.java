package com.d9tilov.android.currency.di;

import com.d9tilov.android.currency.domain.contract.CurrencyInteractor;
import com.d9tilov.android.currency.domain.contract.CurrencyRepo;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class CurrencyDomainModule_ProvideCurrencyInteractorFactory implements Factory<CurrencyInteractor> {
  private final Provider<CurrencyRepo> currencyRepoProvider;

  public CurrencyDomainModule_ProvideCurrencyInteractorFactory(
      Provider<CurrencyRepo> currencyRepoProvider) {
    this.currencyRepoProvider = currencyRepoProvider;
  }

  @Override
  public CurrencyInteractor get() {
    return provideCurrencyInteractor(currencyRepoProvider.get());
  }

  public static CurrencyDomainModule_ProvideCurrencyInteractorFactory create(
      Provider<CurrencyRepo> currencyRepoProvider) {
    return new CurrencyDomainModule_ProvideCurrencyInteractorFactory(currencyRepoProvider);
  }

  public static CurrencyInteractor provideCurrencyInteractor(CurrencyRepo currencyRepo) {
    return Preconditions.checkNotNullFromProvides(CurrencyDomainModule.INSTANCE.provideCurrencyInteractor(currencyRepo));
  }
}
