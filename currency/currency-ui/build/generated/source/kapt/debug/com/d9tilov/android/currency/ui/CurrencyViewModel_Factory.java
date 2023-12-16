package com.d9tilov.android.currency.ui;

import androidx.lifecycle.SavedStateHandle;
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor;
import com.d9tilov.android.currency.observer.contract.CurrencyUpdateObserver;
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
public final class CurrencyViewModel_Factory implements Factory<CurrencyViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<CurrencyInteractor> currencyInteractorProvider;

  private final Provider<CurrencyUpdateObserver> currencyUpdateObserverProvider;

  public CurrencyViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<CurrencyUpdateObserver> currencyUpdateObserverProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.currencyInteractorProvider = currencyInteractorProvider;
    this.currencyUpdateObserverProvider = currencyUpdateObserverProvider;
  }

  @Override
  public CurrencyViewModel get() {
    return newInstance(savedStateHandleProvider.get(), currencyInteractorProvider.get(), currencyUpdateObserverProvider.get());
  }

  public static CurrencyViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<CurrencyInteractor> currencyInteractorProvider,
      Provider<CurrencyUpdateObserver> currencyUpdateObserverProvider) {
    return new CurrencyViewModel_Factory(savedStateHandleProvider, currencyInteractorProvider, currencyUpdateObserverProvider);
  }

  public static CurrencyViewModel newInstance(SavedStateHandle savedStateHandle,
      CurrencyInteractor currencyInteractor, CurrencyUpdateObserver currencyUpdateObserver) {
    return new CurrencyViewModel(savedStateHandle, currencyInteractor, currencyUpdateObserver);
  }
}
