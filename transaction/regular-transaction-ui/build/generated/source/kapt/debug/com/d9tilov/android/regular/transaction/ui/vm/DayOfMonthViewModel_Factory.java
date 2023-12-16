package com.d9tilov.android.regular.transaction.ui.vm;

import androidx.lifecycle.SavedStateHandle;
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
public final class DayOfMonthViewModel_Factory implements Factory<DayOfMonthViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public DayOfMonthViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public DayOfMonthViewModel get() {
    return newInstance(savedStateHandleProvider.get());
  }

  public static DayOfMonthViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new DayOfMonthViewModel_Factory(savedStateHandleProvider);
  }

  public static DayOfMonthViewModel newInstance(SavedStateHandle savedStateHandle) {
    return new DayOfMonthViewModel(savedStateHandle);
  }
}
