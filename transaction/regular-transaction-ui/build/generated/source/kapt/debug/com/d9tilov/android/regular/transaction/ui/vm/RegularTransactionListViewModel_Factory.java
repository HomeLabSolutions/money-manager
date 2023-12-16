package com.d9tilov.android.regular.transaction.ui.vm;

import androidx.lifecycle.SavedStateHandle;
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
public final class RegularTransactionListViewModel_Factory implements Factory<RegularTransactionListViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<RegularTransactionInteractor> regularTransactionInteractorProvider;

  public RegularTransactionListViewModel_Factory(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<RegularTransactionInteractor> regularTransactionInteractorProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.regularTransactionInteractorProvider = regularTransactionInteractorProvider;
  }

  @Override
  public RegularTransactionListViewModel get() {
    return newInstance(savedStateHandleProvider.get(), regularTransactionInteractorProvider.get());
  }

  public static RegularTransactionListViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<RegularTransactionInteractor> regularTransactionInteractorProvider) {
    return new RegularTransactionListViewModel_Factory(savedStateHandleProvider, regularTransactionInteractorProvider);
  }

  public static RegularTransactionListViewModel newInstance(SavedStateHandle savedStateHandle,
      RegularTransactionInteractor regularTransactionInteractor) {
    return new RegularTransactionListViewModel(savedStateHandle, regularTransactionInteractor);
  }
}
