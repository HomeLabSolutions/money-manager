package com.d9tilov.android.currency.di;

import com.d9tilov.android.currency.data.contract.CurrencySource;
import com.d9tilov.android.currency.domain.contract.CurrencyRepo;
import com.d9tilov.android.datastore.PreferencesStore;
import com.d9tilov.android.network.CurrencyApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class CurrencyDataModule_ProvideCurrencyRepoFactory implements Factory<CurrencyRepo> {
  private final Provider<CurrencySource> currencySourceProvider;

  private final Provider<CurrencyApi> currencyApiProvider;

  private final Provider<PreferencesStore> preferencesStoreProvider;

  public CurrencyDataModule_ProvideCurrencyRepoFactory(
      Provider<CurrencySource> currencySourceProvider, Provider<CurrencyApi> currencyApiProvider,
      Provider<PreferencesStore> preferencesStoreProvider) {
    this.currencySourceProvider = currencySourceProvider;
    this.currencyApiProvider = currencyApiProvider;
    this.preferencesStoreProvider = preferencesStoreProvider;
  }

  @Override
  public CurrencyRepo get() {
    return provideCurrencyRepo(currencySourceProvider.get(), currencyApiProvider.get(), preferencesStoreProvider.get());
  }

  public static CurrencyDataModule_ProvideCurrencyRepoFactory create(
      Provider<CurrencySource> currencySourceProvider, Provider<CurrencyApi> currencyApiProvider,
      Provider<PreferencesStore> preferencesStoreProvider) {
    return new CurrencyDataModule_ProvideCurrencyRepoFactory(currencySourceProvider, currencyApiProvider, preferencesStoreProvider);
  }

  public static CurrencyRepo provideCurrencyRepo(CurrencySource currencySource,
      CurrencyApi currencyApi, PreferencesStore preferencesStore) {
    return Preconditions.checkNotNullFromProvides(CurrencyDataModule.INSTANCE.provideCurrencyRepo(currencySource, currencyApi, preferencesStore));
  }
}
