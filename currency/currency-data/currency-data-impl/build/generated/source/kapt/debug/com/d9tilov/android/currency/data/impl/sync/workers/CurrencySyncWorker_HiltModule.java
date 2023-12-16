package com.d9tilov.android.currency.data.impl.sync.workers;

import androidx.hilt.work.WorkerAssistedFactory;
import androidx.work.ListenableWorker;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import javax.annotation.processing.Generated;

@Generated("androidx.hilt.AndroidXHiltProcessor")
@Module
@InstallIn(SingletonComponent.class)
@OriginatingElement(
    topLevelClass = CurrencySyncWorker.class
)
public interface CurrencySyncWorker_HiltModule {
  @Binds
  @IntoMap
  @StringKey("com.d9tilov.android.currency.data.impl.sync.workers.CurrencySyncWorker")
  WorkerAssistedFactory<? extends ListenableWorker> bind(
      CurrencySyncWorker_AssistedFactory factory);
}
