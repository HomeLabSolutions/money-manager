package com.d9tilov.android.currency.data.impl.sync.workers;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class CurrencySyncWorker_AssistedFactory_Impl implements CurrencySyncWorker_AssistedFactory {
  private final CurrencySyncWorker_Factory delegateFactory;

  CurrencySyncWorker_AssistedFactory_Impl(CurrencySyncWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public CurrencySyncWorker create(Context arg0, WorkerParameters arg1) {
    return delegateFactory.get(arg0, arg1);
  }

  public static Provider<CurrencySyncWorker_AssistedFactory> create(
      CurrencySyncWorker_Factory delegateFactory) {
    return InstanceFactory.create(new CurrencySyncWorker_AssistedFactory_Impl(delegateFactory));
  }
}
