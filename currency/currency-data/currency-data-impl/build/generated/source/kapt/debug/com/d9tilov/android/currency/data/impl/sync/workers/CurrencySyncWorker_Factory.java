package com.d9tilov.android.currency.data.impl.sync.workers;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.d9tilov.android.currency.domain.contract.CurrencyInteractor;
import dagger.internal.DaggerGenerated;
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
public final class CurrencySyncWorker_Factory {
  private final Provider<CurrencyInteractor> currencyInteractorProvider;

  public CurrencySyncWorker_Factory(Provider<CurrencyInteractor> currencyInteractorProvider) {
    this.currencyInteractorProvider = currencyInteractorProvider;
  }

  public CurrencySyncWorker get(Context context, WorkerParameters workerParameters) {
    return newInstance(context, workerParameters, currencyInteractorProvider.get());
  }

  public static CurrencySyncWorker_Factory create(
      Provider<CurrencyInteractor> currencyInteractorProvider) {
    return new CurrencySyncWorker_Factory(currencyInteractorProvider);
  }

  public static CurrencySyncWorker newInstance(Context context, WorkerParameters workerParameters,
      CurrencyInteractor currencyInteractor) {
    return new CurrencySyncWorker(context, workerParameters, currencyInteractor);
  }
}
