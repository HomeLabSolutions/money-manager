package com.d9tilov.android.user.di;

import com.d9tilov.android.database.AppDatabase;
import com.d9tilov.android.datastore.PreferencesStore;
import com.d9tilov.android.user.data.contract.UserSource;
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
public final class UserDataModule_ProvideUserSourceFactory implements Factory<UserSource> {
  private final Provider<CoroutineDispatcher> dispatcherProvider;

  private final Provider<AppDatabase> appDatabaseProvider;

  private final Provider<PreferencesStore> preferencesStoreProvider;

  public UserDataModule_ProvideUserSourceFactory(Provider<CoroutineDispatcher> dispatcherProvider,
      Provider<AppDatabase> appDatabaseProvider,
      Provider<PreferencesStore> preferencesStoreProvider) {
    this.dispatcherProvider = dispatcherProvider;
    this.appDatabaseProvider = appDatabaseProvider;
    this.preferencesStoreProvider = preferencesStoreProvider;
  }

  @Override
  public UserSource get() {
    return provideUserSource(dispatcherProvider.get(), appDatabaseProvider.get(), preferencesStoreProvider.get());
  }

  public static UserDataModule_ProvideUserSourceFactory create(
      Provider<CoroutineDispatcher> dispatcherProvider, Provider<AppDatabase> appDatabaseProvider,
      Provider<PreferencesStore> preferencesStoreProvider) {
    return new UserDataModule_ProvideUserSourceFactory(dispatcherProvider, appDatabaseProvider, preferencesStoreProvider);
  }

  public static UserSource provideUserSource(CoroutineDispatcher dispatcher,
      AppDatabase appDatabase, PreferencesStore preferencesStore) {
    return Preconditions.checkNotNullFromProvides(UserDataModule.INSTANCE.provideUserSource(dispatcher, appDatabase, preferencesStore));
  }
}
