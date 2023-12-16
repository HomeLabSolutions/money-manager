package com.d9tilov.android.currency.di;

import com.d9tilov.android.currency.data.contract.CurrencySource;
import com.d9tilov.android.database.AppDatabase;
import com.d9tilov.android.datastore.PreferencesStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineDispatcher;

@ScopeMetadata
@QualifierMetadata("com.d9tilov.android.network.dispatchers.Dispatcher")
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
public final class CurrencyDataModule_ProvideCurrencySourceFactory implements Factory<CurrencySource> {
  private final Provider<CoroutineDispatcher> dispatcherProvider;

  private final Provider<AppDatabase> appDatabaseProvider;

  private final Provider<PreferencesStore> preferencesStoreProvider;

  public CurrencyDataModule_ProvideCurrencySourceFactory(
      Provider<CoroutineDispatcher> dispatcherProvider, Provider<AppDatabase> appDatabaseProvider,
      Provider<PreferencesStore> preferencesStoreProvider) {
    this.dispatcherProvider = dispatcherProvider;
    this.appDatabaseProvider = appDatabaseProvider;
    this.preferencesStoreProvider = preferencesStoreProvider;
  }

  @Override
  public CurrencySource get() {
    return provideCurrencySource(dispatcherProvider.get(), appDatabaseProvider.get(), preferencesStoreProvider.get());
  }

  public static CurrencyDataModule_ProvideCurrencySourceFactory create(
      Provider<CoroutineDispatcher> dispatcherProvider, Provider<AppDatabase> appDatabaseProvider,
      Provider<PreferencesStore> preferencesStoreProvider) {
    return new CurrencyDataModule_ProvideCurrencySourceFactory(dispatcherProvider, appDatabaseProvider, preferencesStoreProvider);
  }

  public static CurrencySource provideCurrencySource(CoroutineDispatcher dispatcher,
      AppDatabase appDatabase, PreferencesStore preferencesStore) {
    return Preconditions.checkNotNullFromProvides(CurrencyDataModule.INSTANCE.provideCurrencySource(dispatcher, appDatabase, preferencesStore));
  }
}
