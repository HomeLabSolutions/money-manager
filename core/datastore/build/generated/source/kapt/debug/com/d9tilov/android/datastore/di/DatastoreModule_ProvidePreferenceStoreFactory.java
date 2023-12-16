package com.d9tilov.android.datastore.di;

import android.content.Context;
import com.d9tilov.android.datastore.PreferencesStore;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class DatastoreModule_ProvidePreferenceStoreFactory implements Factory<PreferencesStore> {
  private final Provider<Context> contextProvider;

  public DatastoreModule_ProvidePreferenceStoreFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public PreferencesStore get() {
    return providePreferenceStore(contextProvider.get());
  }

  public static DatastoreModule_ProvidePreferenceStoreFactory create(
      Provider<Context> contextProvider) {
    return new DatastoreModule_ProvidePreferenceStoreFactory(contextProvider);
  }

  public static PreferencesStore providePreferenceStore(Context context) {
    return Preconditions.checkNotNullFromProvides(DatastoreModule.INSTANCE.providePreferenceStore(context));
  }
}
