package com.d9tilov.android.budget.di;

import com.d9tilov.android.budget.domain.contract.BudgetInteractor;
import com.d9tilov.android.budget.domain.contract.BudgetRepo;
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor;
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
public final class BudgetDomainModule_ProvideBudgetInteractorFactory implements Factory<BudgetInteractor> {
  private final Provider<BudgetRepo> budgetRepoProvider;

  private final Provider<CurrencyInteractor> currencyInteractorProvider;

  public BudgetDomainModule_ProvideBudgetInteractorFactory(Provider<BudgetRepo> budgetRepoProvider,
      Provider<CurrencyInteractor> currencyInteractorProvider) {
    this.budgetRepoProvider = budgetRepoProvider;
    this.currencyInteractorProvider = currencyInteractorProvider;
  }

  @Override
  public BudgetInteractor get() {
    return provideBudgetInteractor(budgetRepoProvider.get(), currencyInteractorProvider.get());
  }

  public static BudgetDomainModule_ProvideBudgetInteractorFactory create(
      Provider<BudgetRepo> budgetRepoProvider,
      Provider<CurrencyInteractor> currencyInteractorProvider) {
    return new BudgetDomainModule_ProvideBudgetInteractorFactory(budgetRepoProvider, currencyInteractorProvider);
  }

  public static BudgetInteractor provideBudgetInteractor(BudgetRepo budgetRepo,
      CurrencyInteractor currencyInteractor) {
    return Preconditions.checkNotNullFromProvides(BudgetDomainModule.INSTANCE.provideBudgetInteractor(budgetRepo, currencyInteractor));
  }
}
