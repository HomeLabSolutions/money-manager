package com.d9tilov.android.transaction.di;

import com.d9tilov.android.database.AppDatabase;
import com.d9tilov.android.datastore.PreferencesStore;
import com.d9tilov.android.transaction.data.contract.TransactionSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineDispatcher;

@ScopeMetadata("dagger.hilt.android.scopes.ActivityRetainedScoped")
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
public final class TransactionDataModule_ProvideTransactionLocalSourceFactory implements Factory<TransactionSource> {
  private final Provider<CoroutineDispatcher> dispatcherProvider;

  private final Provider<PreferencesStore> preferencesStoreProvider;

  private final Provider<AppDatabase> appDatabaseProvider;

  public TransactionDataModule_ProvideTransactionLocalSourceFactory(
      Provider<CoroutineDispatcher> dispatcherProvider,
      Provider<PreferencesStore> preferencesStoreProvider,
      Provider<AppDatabase> appDatabaseProvider) {
    this.dispatcherProvider = dispatcherProvider;
    this.preferencesStoreProvider = preferencesStoreProvider;
    this.appDatabaseProvider = appDatabaseProvider;
  }

  @Override
  public TransactionSource get() {
    return provideTransactionLocalSource(dispatcherProvider.get(), preferencesStoreProvider.get(), appDatabaseProvider.get());
  }

  public static TransactionDataModule_ProvideTransactionLocalSourceFactory create(
      Provider<CoroutineDispatcher> dispatcherProvider,
      Provider<PreferencesStore> preferencesStoreProvider,
      Provider<AppDatabase> appDatabaseProvider) {
    return new TransactionDataModule_ProvideTransactionLocalSourceFactory(dispatcherProvider, preferencesStoreProvider, appDatabaseProvider);
  }

  public static TransactionSource provideTransactionLocalSource(CoroutineDispatcher dispatcher,
      PreferencesStore preferencesStore, AppDatabase appDatabase) {
    return Preconditions.checkNotNullFromProvides(TransactionDataModule.INSTANCE.provideTransactionLocalSource(dispatcher, preferencesStore, appDatabase));
  }
}
