package com.d9tilov.android.category.ui.vm;

import androidx.lifecycle.SavedStateHandle;
import com.d9tilov.android.billing.domain.contract.BillingInteractor;
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
public final class CategoryIconGridViewModel_Factory implements Factory<CategoryIconGridViewModel> {
  private final Provider<BillingInteractor> billingInteractorProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public CategoryIconGridViewModel_Factory(Provider<BillingInteractor> billingInteractorProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.billingInteractorProvider = billingInteractorProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public CategoryIconGridViewModel get() {
    return newInstance(billingInteractorProvider.get(), savedStateHandleProvider.get());
  }

  public static CategoryIconGridViewModel_Factory create(
      Provider<BillingInteractor> billingInteractorProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new CategoryIconGridViewModel_Factory(billingInteractorProvider, savedStateHandleProvider);
  }

  public static CategoryIconGridViewModel newInstance(BillingInteractor billingInteractor,
      SavedStateHandle savedStateHandle) {
    return new CategoryIconGridViewModel(billingInteractor, savedStateHandle);
  }
}
