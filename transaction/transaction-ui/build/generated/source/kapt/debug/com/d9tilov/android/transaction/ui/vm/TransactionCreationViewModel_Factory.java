package com.d9tilov.android.transaction.ui.vm;

import androidx.lifecycle.SavedStateHandle;
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
public final class TransactionCreationViewModel_Factory implements Factory<TransactionCreationViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<TransactionInteractor> transactionInteractorProvider;

  private final Provider<CategoryInteractor> categoryInteractorProvider;

  public TransactionCreationViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<TransactionInteractor> transactionInteractorProvider,
      Provider<CategoryInteractor> categoryInteractorProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.transactionInteractorProvider = transactionInteractorProvider;
    this.categoryInteractorProvider = categoryInteractorProvider;
  }

  @Override
  public TransactionCreationViewModel get() {
    return newInstance(savedStateHandleProvider.get(), transactionInteractorProvider.get(), categoryInteractorProvider.get());
  }

  public static TransactionCreationViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<TransactionInteractor> transactionInteractorProvider,
      Provider<CategoryInteractor> categoryInteractorProvider) {
    return new TransactionCreationViewModel_Factory(savedStateHandleProvider, transactionInteractorProvider, categoryInteractorProvider);
  }

  public static TransactionCreationViewModel newInstance(SavedStateHandle savedStateHandle,
      TransactionInteractor transactionInteractor, CategoryInteractor categoryInteractor) {
    return new TransactionCreationViewModel(savedStateHandle, transactionInteractor, categoryInteractor);
  }
}
