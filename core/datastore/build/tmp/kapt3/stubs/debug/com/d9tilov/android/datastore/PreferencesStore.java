package com.d9tilov.android.datastore;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\b\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0011\u0010\u0010\u001a\u00020\u0011H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012J\u0019\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u0007H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015J\u0019\u0010\u0016\u001a\u00020\u00112\u0006\u0010\r\u001a\u00020\u000eH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0019"}, d2 = {"Lcom/d9tilov/android/datastore/PreferencesStore;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "backupData", "Lkotlinx/coroutines/flow/Flow;", "", "getBackupData", "()Lkotlinx/coroutines/flow/Flow;", "dataStore", "Landroidx/datastore/core/DataStore;", "Landroidx/datastore/preferences/core/Preferences;", "uid", "", "getUid", "clearAllData", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateLastBackupDate", "date", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateUid", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "datastore_debug"})
public final class PreferencesStore {
    @org.jetbrains.annotations.NotNull
    private final androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences> dataStore = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<java.lang.String> uid = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<java.lang.Long> backupData = null;
    @org.jetbrains.annotations.NotNull
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.Long> PREFERENCE_LAST_BACKUP_DATE_KEY = null;
    @org.jetbrains.annotations.NotNull
    private static final androidx.datastore.preferences.core.Preferences.Key<java.lang.String> PREFERENCE_CLIENT_UID_KEY = null;
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.datastore.PreferencesStore.Companion Companion = null;
    
    public PreferencesStore(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.lang.String> getUid() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object updateUid(@org.jetbrains.annotations.NotNull
    java.lang.String uid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.lang.Long> getBackupData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object updateLastBackupDate(long date, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object clearAllData(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/d9tilov/android/datastore/PreferencesStore$Companion;", "", "()V", "PREFERENCE_CLIENT_UID_KEY", "Landroidx/datastore/preferences/core/Preferences$Key;", "", "PREFERENCE_LAST_BACKUP_DATE_KEY", "", "datastore_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}