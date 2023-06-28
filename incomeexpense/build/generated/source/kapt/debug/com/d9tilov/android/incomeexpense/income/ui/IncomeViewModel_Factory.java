package com.d9tilov.android.incomeexpense.income.ui;

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
public final class IncomeViewModel_Factory implements Factory<IncomeViewModel> {
  private final Provider<CategoryInteractor> categoryInteractorProvider;

  private final Provider<TransactionInteractor> transactionInteractorProvider;

  public IncomeViewModel_Factory(Provider<CategoryInteractor> categoryInteractorProvider,
      Provider<TransactionInteractor> transactionInteractorProvider) {
    this.categoryInteractorProvider = categoryInteractorProvider;
    this.transactionInteractorProvider = transactionInteractorProvider;
  }

  @Override
  public IncomeViewModel get() {
    return newInstance(categoryInteractorProvider.get(), transactionInteractorProvider.get());
  }

  public static IncomeViewModel_Factory create(
      Provider<CategoryInteractor> categoryInteractorProvider,
      Provider<TransactionInteractor> transactionInteractorProvider) {
    return new IncomeViewModel_Factory(categoryInteractorProvider, transactionInteractorProvider);
  }

  public static IncomeViewModel newInstance(CategoryInteractor categoryInteractor,
      TransactionInteractor transactionInteractor) {
    return new IncomeViewModel(categoryInteractor, transactionInteractor);
  }
}
