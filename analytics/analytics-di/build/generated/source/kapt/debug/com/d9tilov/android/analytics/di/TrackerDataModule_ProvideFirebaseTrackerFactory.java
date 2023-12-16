package com.d9tilov.android.analytics.di;

import com.d9tilov.android.datastore.PreferencesStore;
import com.google.firebase.analytics.FirebaseAnalytics;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineScope;

@ScopeMetadata("dagger.hilt.android.scopes.ActivityRetainedScoped")
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
public final class TrackerDataModule_ProvideFirebaseTrackerFactory implements Factory<FirebaseAnalytics> {
  private final Provider<CoroutineScope> coroutineScopeProvider;

  private final Provider<PreferencesStore> preferencesStoreProvider;

  public TrackerDataModule_ProvideFirebaseTrackerFactory(
      Provider<CoroutineScope> coroutineScopeProvider,
      Provider<PreferencesStore> preferencesStoreProvider) {
    this.coroutineScopeProvider = coroutineScopeProvider;
    this.preferencesStoreProvider = preferencesStoreProvider;
  }

  @Override
  public FirebaseAnalytics get() {
    return provideFirebaseTracker(coroutineScopeProvider.get(), preferencesStoreProvider.get());
  }

  public static TrackerDataModule_ProvideFirebaseTrackerFactory create(
      Provider<CoroutineScope> coroutineScopeProvider,
      Provider<PreferencesStore> preferencesStoreProvider) {
    return new TrackerDataModule_ProvideFirebaseTrackerFactory(coroutineScopeProvider, preferencesStoreProvider);
  }

  public static FirebaseAnalytics provideFirebaseTracker(CoroutineScope coroutineScope,
      PreferencesStore preferencesStore) {
    return Preconditions.checkNotNullFromProvides(TrackerDataModule.INSTANCE.provideFirebaseTracker(coroutineScope, preferencesStore));
  }
}
