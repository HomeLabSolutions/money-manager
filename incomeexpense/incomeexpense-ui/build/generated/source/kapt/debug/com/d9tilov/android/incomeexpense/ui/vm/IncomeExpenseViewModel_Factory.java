package com.d9tilov.android.incomeexpense.ui.vm;

import com.d9tilov.android.billing.domain.contract.BillingInteractor;
import com.d9tilov.android.category.domain.contract.CategoryInteractor;
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
public final class IncomeExpenseViewModel_Factory implements Factory<IncomeExpenseViewModel> {
  private final Provider<BillingInteractor> billingInteractorProvider;

  private final Provider<CurrencyInteractor> currencyInteractorProvider;

  private final Provider<CategoryInteractor> categoryInteractorProvider;

  private final Provider<TransactionInteractor> transactionInteractorProvider;

  public IncomeExpenseViewModel_Factory(Provider<BillingInteractor> billingInteractorProvider,
      Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<CategoryInteractor> categoryInteractorProvider,
      Provider<TransactionInteractor> transactionInteractorProvider) {
    this.billingInteractorProvider = billingInteractorProvider;
    this.currencyInteractorProvider = currencyInteractorProvider;
    this.categoryInteractorProvider = categoryInteractorProvider;
    this.transactionInteractorProvider = transactionInteractorProvider;
  }

  @Override
  public IncomeExpenseViewModel get() {
    return newInstance(billingInteractorProvider.get(), currencyInteractorProvider.get(), categoryInteractorProvider.get(), transactionInteractorProvider.get());
  }

  public static IncomeExpenseViewModel_Factory create(
      Provider<BillingInteractor> billingInteractorProvider,
      Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<CategoryInteractor> categoryInteractorProvider,
      Provider<TransactionInteractor> transactionInteractorProvider) {
    return new IncomeExpenseViewModel_Factory(billingInteractorProvider, currencyInteractorProvider, categoryInteractorProvider, transactionInteractorProvider);
  }

  public static IncomeExpenseViewModel newInstance(BillingInteractor billingInteractor,
      CurrencyInteractor currencyInteractor, CategoryInteractor categoryInteractor,
      TransactionInteractor transactionInteractor) {
    return new IncomeExpenseViewModel(billingInteractor, currencyInteractor, categoryInteractor, transactionInteractor);
  }
}
