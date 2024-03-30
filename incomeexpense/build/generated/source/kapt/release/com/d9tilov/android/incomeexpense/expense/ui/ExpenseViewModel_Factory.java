package com.d9tilov.android.incomeexpense.expense.ui;

import com.d9tilov.android.billing.domain.contract.BillingInteractor;
import com.d9tilov.android.category.domain.contract.CategoryInteractor;
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
public final class ExpenseViewModel_Factory implements Factory<ExpenseViewModel> {
  private final Provider<CategoryInteractor> categoryInteractorProvider;

  private final Provider<BillingInteractor> billingInteractorProvider;

  private final Provider<TransactionInteractor> transactionInteractorProvider;

  public ExpenseViewModel_Factory(Provider<CategoryInteractor> categoryInteractorProvider,
      Provider<BillingInteractor> billingInteractorProvider,
      Provider<TransactionInteractor> transactionInteractorProvider) {
    this.categoryInteractorProvider = categoryInteractorProvider;
    this.billingInteractorProvider = billingInteractorProvider;
    this.transactionInteractorProvider = transactionInteractorProvider;
  }

  @Override
  public ExpenseViewModel get() {
    return newInstance(categoryInteractorProvider.get(), billingInteractorProvider.get(), transactionInteractorProvider.get());
  }

  public static ExpenseViewModel_Factory create(
      Provider<CategoryInteractor> categoryInteractorProvider,
      Provider<BillingInteractor> billingInteractorProvider,
      Provider<TransactionInteractor> transactionInteractorProvider) {
    return new ExpenseViewModel_Factory(categoryInteractorProvider, billingInteractorProvider, transactionInteractorProvider);
  }

  public static ExpenseViewModel newInstance(CategoryInteractor categoryInteractor,
      BillingInteractor billingInteractor, TransactionInteractor transactionInteractor) {
    return new ExpenseViewModel(categoryInteractor, billingInteractor, transactionInteractor);
  }
}
