package com.d9tilov.android.statistics.ui.vm;

import com.d9tilov.android.billing.domain.contract.BillingInteractor;
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor;
import com.d9tilov.android.transaction.domain.contract.TransactionInteractor;
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
public final class StatisticsViewModel_Factory implements Factory<StatisticsViewModel> {
  private final Provider<TransactionInteractor> transactionInteractorProvider;

  private final Provider<CurrencyInteractor> currencyInteractorProvider;

  private final Provider<BillingInteractor> billingInteractorProvider;

  public StatisticsViewModel_Factory(Provider<TransactionInteractor> transactionInteractorProvider,
      Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<BillingInteractor> billingInteractorProvider) {
    this.transactionInteractorProvider = transactionInteractorProvider;
    this.currencyInteractorProvider = currencyInteractorProvider;
    this.billingInteractorProvider = billingInteractorProvider;
  }

  @Override
  public StatisticsViewModel get() {
    return newInstance(transactionInteractorProvider.get(), currencyInteractorProvider.get(), billingInteractorProvider.get());
  }

  public static StatisticsViewModel_Factory create(
      Provider<TransactionInteractor> transactionInteractorProvider,
      Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<BillingInteractor> billingInteractorProvider) {
    return new StatisticsViewModel_Factory(transactionInteractorProvider, currencyInteractorProvider, billingInteractorProvider);
  }

  public static StatisticsViewModel newInstance(TransactionInteractor transactionInteractor,
      CurrencyInteractor currencyInteractor, BillingInteractor billingInteractor) {
    return new StatisticsViewModel(transactionInteractor, currencyInteractor, billingInteractor);
  }
}
