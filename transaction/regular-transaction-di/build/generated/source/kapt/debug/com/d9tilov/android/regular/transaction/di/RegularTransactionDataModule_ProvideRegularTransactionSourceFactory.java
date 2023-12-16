package com.d9tilov.android.regular.transaction.di;

import com.d9tilov.android.database.AppDatabase;
import com.d9tilov.android.datastore.PreferencesStore;
import com.d9tilov.android.regular.transaction.data.contract.RegularTransactionSource;
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
public final class RegularTransactionDataModule_ProvideRegularTransactionSourceFactory implements Factory<RegularTransactionSource> {
  private final Provider<CoroutineDispatcher> dispatcherProvider;

  private final Provider<PreferencesStore> preferencesStoreProvider;

  private final Provider<AppDatabase> appDatabaseProvider;

  public RegularTransactionDataModule_ProvideRegularTransactionSourceFactory(
      Provider<CoroutineDispatcher> dispatcherProvider,
      Provider<PreferencesStore> preferencesStoreProvider,
      Provider<AppDatabase> appDatabaseProvider) {
    this.dispatcherProvider = dispatcherProvider;
    this.preferencesStoreProvider = preferencesStoreProvider;
    this.appDatabaseProvider = appDatabaseProvider;
  }

  @Override
  public RegularTransactionSource get() {
    return provideRegularTransactionSource(dispatcherProvider.get(), preferencesStoreProvider.get(), appDatabaseProvider.get());
  }

  public static RegularTransactionDataModule_ProvideRegularTransactionSourceFactory create(
      Provider<CoroutineDispatcher> dispatcherProvider,
      Provider<PreferencesStore> preferencesStoreProvider,
      Provider<AppDatabase> appDatabaseProvider) {
    return new RegularTransactionDataModule_ProvideRegularTransactionSourceFactory(dispatcherProvider, preferencesStoreProvider, appDatabaseProvider);
  }

  public static RegularTransactionSource provideRegularTransactionSource(
      CoroutineDispatcher dispatcher, PreferencesStore preferencesStore, AppDatabase appDatabase) {
    return Preconditions.checkNotNullFromProvides(RegularTransactionDataModule.INSTANCE.provideRegularTransactionSource(dispatcher, preferencesStore, appDatabase));
  }
}
