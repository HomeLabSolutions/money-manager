package com.d9tilov.android.analytics.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007\u00a8\u0006\t"}, d2 = {"Lcom/d9tilov/android/analytics/di/TrackerDataModule;", "", "()V", "provideFirebaseTracker", "Lcom/google/firebase/analytics/FirebaseAnalytics;", "coroutineScope", "Lkotlinx/coroutines/CoroutineScope;", "preferencesStore", "Lcom/d9tilov/android/datastore/PreferencesStore;", "analytics-di_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.android.components.ActivityRetainedComponent.class})
public final class TrackerDataModule {
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.analytics.di.TrackerDataModule INSTANCE = null;
    
    private TrackerDataModule() {
        super();
    }
    
    @dagger.Provides
    @dagger.hilt.android.scopes.ActivityRetainedScoped
    @org.jetbrains.annotations.NotNull
    public final com.google.firebase.analytics.FirebaseAnalytics provideFirebaseTracker(@org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineScope coroutineScope, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.datastore.PreferencesStore preferencesStore) {
        return null;
    }
}