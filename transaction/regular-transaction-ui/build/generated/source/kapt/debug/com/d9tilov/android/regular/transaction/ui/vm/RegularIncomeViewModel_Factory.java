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
public final class RegularIncomeViewModel_Factory implements Factory<RegularIncomeViewModel> {
  private final Provider<RegularTransactionInteractor> regularTransactionInteractorProvider;

  public RegularIncomeViewModel_Factory(
      Provider<RegularTransactionInteractor> regularTransactionInteractorProvider) {
    this.regularTransactionInteractorProvider = regularTransactionInteractorProvider;
  }

  @Override
  public RegularIncomeViewModel get() {
    return newInstance(regularTransactionInteractorProvider.get());
  }

  public static RegularIncomeViewModel_Factory create(
      Provider<RegularTransactionInteractor> regularTransactionInteractorProvider) {
    return new RegularIncomeViewModel_Factory(regularTransactionInteractorProvider);
  }

  public static RegularIncomeViewModel newInstance(
      RegularTransactionInteractor regularTransactionInteractor) {
    return new RegularIncomeViewModel(regularTransactionInteractor);
  }
}
