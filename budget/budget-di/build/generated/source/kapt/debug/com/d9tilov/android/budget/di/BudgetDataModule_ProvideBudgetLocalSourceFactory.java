package com.d9tilov.android.budget.di;

import com.d9tilov.android.budget.data.contract.BudgetSource;
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

@ScopeMetadata("javax.inject.Singleton")
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
public final class BudgetDataModule_ProvideBudgetLocalSourceFactory implements Factory<BudgetSource> {
  private final Provider<CoroutineDispatcher> dispatcherProvider;

  private final Provider<PreferencesStore> preferencesStoreProvider;

  private final Provider<AppDatabase> databaseProvider;

  public BudgetDataModule_ProvideBudgetLocalSourceFactory(
      Provider<CoroutineDispatcher> dispatcherProvider,
      Provider<PreferencesStore> preferencesStoreProvider, Provider<AppDatabase> databaseProvider) {
    this.dispatcherProvider = dispatcherProvider;
    this.preferencesStoreProvider = preferencesStoreProvider;
    this.databaseProvider = databaseProvider;
  }

  @Override
  public BudgetSource get() {
    return provideBudgetLocalSource(dispatcherProvider.get(), preferencesStoreProvider.get(), databaseProvider.get());
  }

  public static BudgetDataModule_ProvideBudgetLocalSourceFactory create(
      Provider<CoroutineDispatcher> dispatcherProvider,
      Provider<PreferencesStore> preferencesStoreProvider, Provider<AppDatabase> databaseProvider) {
    return new BudgetDataModule_ProvideBudgetLocalSourceFactory(dispatcherProvider, preferencesStoreProvider, databaseProvider);
  }

  public static BudgetSource provideBudgetLocalSource(CoroutineDispatcher dispatcher,
      PreferencesStore preferencesStore, AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(BudgetDataModule.INSTANCE.provideBudgetLocalSource(dispatcher, preferencesStore, database));
  }
}
