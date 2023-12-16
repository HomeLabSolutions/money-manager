package com.d9tilov.android.user.data.impl;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u001f\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0019\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJ\u0011\u0010\r\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0010\u0010\u0010\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\n0\u0011H\u0016J\u0011\u0010\u0012\u001a\u00020\u0013H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0011\u0010\u0014\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0011\u0010\u0015\u001a\u00020\u0016H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0019\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u0013H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001a"}, d2 = {"Lcom/d9tilov/android/user/data/impl/UserLocalSource;", "Lcom/d9tilov/android/user/data/contract/UserSource;", "dispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "preferencesStore", "Lcom/d9tilov/android/datastore/PreferencesStore;", "userDao", "Lcom/d9tilov/android/database/dao/UserDao;", "(Lkotlinx/coroutines/CoroutineDispatcher;Lcom/d9tilov/android/datastore/PreferencesStore;Lcom/d9tilov/android/database/dao/UserDao;)V", "createUserOrRestore", "Lcom/d9tilov/android/user/domain/model/UserProfile;", "userProfile", "(Lcom/d9tilov/android/user/domain/model/UserProfile;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteUser", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCurrentUser", "Lkotlinx/coroutines/flow/Flow;", "getFiscalDay", "", "prepopulateCompleted", "showPrepopulate", "", "updateFiscalDay", "fiscalDay", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "user-data-impl_debug"})
public final class UserLocalSource implements com.d9tilov.android.user.data.contract.UserSource {
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.CoroutineDispatcher dispatcher = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.datastore.PreferencesStore preferencesStore = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.database.dao.UserDao userDao = null;
    
    public UserLocalSource(@com.d9tilov.android.network.dispatchers.Dispatcher(moneyManagerDispatcher = com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers.IO)
    @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineDispatcher dispatcher, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.datastore.PreferencesStore preferencesStore, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.dao.UserDao userDao) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object createUserOrRestore(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.user.domain.model.UserProfile userProfile, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.d9tilov.android.user.domain.model.UserProfile> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object updateFiscalDay(int fiscalDay, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object showPrepopulate(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object prepopulateCompleted(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object getFiscalDay(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @java.lang.Override
    @kotlin.OptIn(markerClass = {kotlinx.coroutines.ExperimentalCoroutinesApi.class})
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<com.d9tilov.android.user.domain.model.UserProfile> getCurrentUser() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object deleteUser(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}