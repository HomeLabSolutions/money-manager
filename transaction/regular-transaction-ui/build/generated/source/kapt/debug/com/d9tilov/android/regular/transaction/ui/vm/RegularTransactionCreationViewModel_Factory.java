package com.d9tilov.android.regular.transaction.ui.vm;

import androidx.lifecycle.SavedStateHandle;
import com.d9tilov.android.category.domain.contract.CategoryInteractor;
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
public final class RegularTransactionCreationViewModel_Factory implements Factory<RegularTransactionCreationViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<RegularTransactionInteractor> regularTransactionInteractorProvider;

  private final Provider<CategoryInteractor> categoryInteractorProvider;

  public RegularTransactionCreationViewModel_Factory(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<RegularTransactionInteractor> regularTransactionInteractorProvider,
      Provider<CategoryInteractor> categoryInteractorProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.regularTransactionInteractorProvider = regularTransactionInteractorProvider;
    this.categoryInteractorProvider = categoryInteractorProvider;
  }

  @Override
  public RegularTransactionCreationViewModel get() {
    return newInstance(savedStateHandleProvider.get(), regularTransactionInteractorProvider.get(), categoryInteractorProvider.get());
  }

  public static RegularTransactionCreationViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<RegularTransactionInteractor> regularTransactionInteractorProvider,
      Provider<CategoryInteractor> categoryInteractorProvider) {
    return new RegularTransactionCreationViewModel_Factory(savedStateHandleProvider, regularTransactionInteractorProvider, categoryInteractorProvider);
  }

  public static RegularTransactionCreationViewModel newInstance(SavedStateHandle savedStateHandle,
      RegularTransactionInteractor regularTransactionInteractor,
      CategoryInteractor categoryInteractor) {
    return new RegularTransactionCreationViewModel(savedStateHandle, regularTransactionInteractor, categoryInteractor);
  }
}
