package com.d9tilov.android.datastore.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2 = {"Lcom/d9tilov/android/datastore/di/DatastoreModule;", "", "()V", "providePreferenceStore", "Lcom/d9tilov/android/datastore/PreferencesStore;", "context", "Landroid/content/Context;", "datastore_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class DatastoreModule {
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.datastore.di.DatastoreModule INSTANCE = null;
    
    private DatastoreModule() {
        super();
    }
    
    @dagger.Provides
    @javax.inject.Singleton
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.datastore.PreferencesStore providePreferenceStore(@dagger.hilt.android.qualifiers.ApplicationContext
    @org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return null;
    }
}