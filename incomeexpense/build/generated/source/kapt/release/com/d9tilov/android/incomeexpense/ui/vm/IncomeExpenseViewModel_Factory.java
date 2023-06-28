package com.d9tilov.android.incomeexpense.ui.vm;

import com.d9tilov.android.billing.domain.contract.BillingInteractor;
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor;
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
public final class IncomeExpenseViewModel_Factory implements Factory<IncomeExpenseViewModel> {
  private final Provider<CurrencyInteractor> currencyInteractorProvider;

  private final Provider<BillingInteractor> billingInteractorProvider;

  public IncomeExpenseViewModel_Factory(Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<BillingInteractor> billingInteractorProvider) {
    this.currencyInteractorProvider = currencyInteractorProvider;
    this.billingInteractorProvider = billingInteractorProvider;
  }

  @Override
  public IncomeExpenseViewModel get() {
    return newInstance(currencyInteractorProvider.get(), billingInteractorProvider.get());
  }

  public static IncomeExpenseViewModel_Factory create(
      Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<BillingInteractor> billingInteractorProvider) {
    return new IncomeExpenseViewModel_Factory(currencyInteractorProvider, billingInteractorProvider);
  }

  public static IncomeExpenseViewModel newInstance(CurrencyInteractor currencyInteractor,
      BillingInteractor billingInteractor) {
    return new IncomeExpenseViewModel(currencyInteractor, billingInteractor);
  }
}
