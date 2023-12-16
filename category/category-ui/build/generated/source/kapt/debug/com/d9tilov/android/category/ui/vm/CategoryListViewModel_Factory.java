package com.d9tilov.android.category.ui.vm;

import androidx.lifecycle.SavedStateHandle;
import com.d9tilov.android.category.domain.contract.CategoryInteractor;
import com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor;
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
public final class CategoryListViewModel_Factory implements Factory<CategoryListViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<CategoryInteractor> categoryInteractorProvider;

  private final Provider<TransactionInteractor> transactionInteractorProvider;

  private final Provider<RegularTransactionInteractor> regularTransactionInteractorProvider;

  public CategoryListViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<CategoryInteractor> categoryInteractorProvider,
      Provider<TransactionInteractor> transactionInteractorProvider,
      Provider<RegularTransactionInteractor> regularTransactionInteractorProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.categoryInteractorProvider = categoryInteractorProvider;
    this.transactionInteractorProvider = transactionInteractorProvider;
    this.regularTransactionInteractorProvider = regularTransactionInteractorProvider;
  }

  @Override
  public CategoryListViewModel get() {
    return newInstance(savedStateHandleProvider.get(), categoryInteractorProvider.get(), transactionInteractorProvider.get(), regularTransactionInteractorProvider.get());
  }

  public static CategoryListViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<CategoryInteractor> categoryInteractorProvider,
      Provider<TransactionInteractor> transactionInteractorProvider,
      Provider<RegularTransactionInteractor> regularTransactionInteractorProvider) {
    return new CategoryListViewModel_Factory(savedStateHandleProvider, categoryInteractorProvider, transactionInteractorProvider, regularTransactionInteractorProvider);
  }

  public static CategoryListViewModel newInstance(SavedStateHandle savedStateHandle,
      CategoryInteractor categoryInteractor, TransactionInteractor transactionInteractor,
      RegularTransactionInteractor regularTransactionInteractor) {
    return new CategoryListViewModel(savedStateHandle, categoryInteractor, transactionInteractor, regularTransactionInteractor);
  }
}
