package com.d9tilov.android.regular.transaction.ui.vm;

import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor;
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
public final class RegularExpenseViewModel_Factory implements Factory<RegularExpenseViewModel> {
  private final Provider<RegularTransactionInteractor> regularTransactionInteractorProvider;

  public RegularExpenseViewModel_Factory(
      Provider<RegularTransactionInteractor> regularTransactionInteractorProvider) {
    this.regularTransactionInteractorProvider = regularTransactionInteractorProvider;
  }

  @Override
  public RegularExpenseViewModel get() {
    return newInstance(regularTransactionInteractorProvider.get());
  }

  public static RegularExpenseViewModel_Factory create(
      Provider<RegularTransactionInteractor> regularTransactionInteractorProvider) {
    return new RegularExpenseViewModel_Factory(regularTransactionInteractorProvider);
  }

  public static RegularExpenseViewModel newInstance(
      RegularTransactionInteractor regularTransactionInteractor) {
    return new RegularExpenseViewModel(regularTransactionInteractor);
  }
}
