package com.d9tilov.android.statistics.ui.vm;

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
public final class StatisticsDetailsViewModel_Factory implements Factory<StatisticsDetailsViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<TransactionInteractor> transactionInteractorProvider;

  private final Provider<CategoryInteractor> categoryInteractorProvider;

  public StatisticsDetailsViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<TransactionInteractor> transactionInteractorProvider,
      Provider<CategoryInteractor> categoryInteractorProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.transactionInteractorProvider = transactionInteractorProvider;
    this.categoryInteractorProvider = categoryInteractorProvider;
  }

  @Override
  public StatisticsDetailsViewModel get() {
    return newInstance(savedStateHandleProvider.get(), transactionInteractorProvider.get(), categoryInteractorProvider.get());
  }

  public static StatisticsDetailsViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<TransactionInteractor> transactionInteractorProvider,
      Provider<CategoryInteractor> categoryInteractorProvider) {
    return new StatisticsDetailsViewModel_Factory(savedStateHandleProvider, transactionInteractorProvider, categoryInteractorProvider);
  }

  public static StatisticsDetailsViewModel newInstance(SavedStateHandle savedStateHandle,
      TransactionInteractor transactionInteractor, CategoryInteractor categoryInteractor) {
    return new StatisticsDetailsViewModel(savedStateHandle, transactionInteractor, categoryInteractor);
  }
}
