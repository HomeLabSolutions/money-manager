package com.d9tilov.android.currency.di;

import com.d9tilov.android.budget.domain.contract.BudgetInteractor;
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor;
import com.d9tilov.android.currency.observer.contract.CurrencyUpdateObserver;
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
public final class CurrencyDomainModule_ProvideCurrencyObserverFactory implements Factory<CurrencyUpdateObserver> {
  private final Provider<CurrencyInteractor> currencyInteractorProvider;

  private final Provider<BudgetInteractor> budgetInteractorProvider;

  public CurrencyDomainModule_ProvideCurrencyObserverFactory(
      Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<BudgetInteractor> budgetInteractorProvider) {
    this.currencyInteractorProvider = currencyInteractorProvider;
    this.budgetInteractorProvider = budgetInteractorProvider;
  }

  @Override
  public CurrencyUpdateObserver get() {
    return provideCurrencyObserver(currencyInteractorProvider.get(), budgetInteractorProvider.get());
  }

  public static CurrencyDomainModule_ProvideCurrencyObserverFactory create(
      Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<BudgetInteractor> budgetInteractorProvider) {
    return new CurrencyDomainModule_ProvideCurrencyObserverFactory(currencyInteractorProvider, budgetInteractorProvider);
  }

  public static CurrencyUpdateObserver provideCurrencyObserver(
      CurrencyInteractor currencyInteractor, BudgetInteractor budgetInteractor) {
    return Preconditions.checkNotNullFromProvides(CurrencyDomainModule.INSTANCE.provideCurrencyObserver(currencyInteractor, budgetInteractor));
  }
}
