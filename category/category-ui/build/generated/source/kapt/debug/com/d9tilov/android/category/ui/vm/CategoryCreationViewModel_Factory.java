package com.d9tilov.android.category.ui.vm;

import androidx.lifecycle.SavedStateHandle;
import com.d9tilov.android.billing.domain.contract.BillingInteractor;
import com.d9tilov.android.category.domain.contract.CategoryInteractor;
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
public final class CategoryCreationViewModel_Factory implements Factory<CategoryCreationViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<CategoryInteractor> categoryInteractorProvider;

  private final Provider<BillingInteractor> billingInteractorProvider;

  public CategoryCreationViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<CategoryInteractor> categoryInteractorProvider,
      Provider<BillingInteractor> billingInteractorProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.categoryInteractorProvider = categoryInteractorProvider;
    this.billingInteractorProvider = billingInteractorProvider;
  }

  @Override
  public CategoryCreationViewModel get() {
    return newInstance(savedStateHandleProvider.get(), categoryInteractorProvider.get(), billingInteractorProvider.get());
  }

  public static CategoryCreationViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<CategoryInteractor> categoryInteractorProvider,
      Provider<BillingInteractor> billingInteractorProvider) {
    return new CategoryCreationViewModel_Factory(savedStateHandleProvider, categoryInteractorProvider, billingInteractorProvider);
  }

  public static CategoryCreationViewModel newInstance(SavedStateHandle savedStateHandle,
      CategoryInteractor categoryInteractor, BillingInteractor billingInteractor) {
    return new CategoryCreationViewModel(savedStateHandle, categoryInteractor, billingInteractor);
  }
}
