package com.d9tilov.android.user.data.impl;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\u0011\u0010\t\u001a\u00020\nH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ\u0010\u0010\f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00060\rH\u0016J\u0011\u0010\u000e\u001a\u00020\u000fH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ\u0011\u0010\u0010\u001a\u00020\nH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ\u0011\u0010\u0011\u001a\u00020\u0012H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ\u0019\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\u000fH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0016"}, d2 = {"Lcom/d9tilov/android/user/data/impl/UserDataRepo;", "Lcom/d9tilov/android/user/domain/contract/UserRepo;", "userLocalSource", "Lcom/d9tilov/android/user/data/contract/UserSource;", "(Lcom/d9tilov/android/user/data/contract/UserSource;)V", "create", "Lcom/d9tilov/android/user/domain/model/UserProfile;", "entity", "(Lcom/d9tilov/android/user/domain/model/UserProfile;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCurrentUser", "Lkotlinx/coroutines/flow/Flow;", "getFiscalDay", "", "prepopulateCompleted", "showPrepopulate", "", "updateFiscalDay", "fiscalDay", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "user-data-impl_debug"})
public final class UserDataRepo implements com.d9tilov.android.user.domain.contract.UserRepo {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.user.data.contract.UserSource userLocalSource = null;
    
    public UserDataRepo(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.user.data.contract.UserSource userLocalSource) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<com.d9tilov.android.user.domain.model.UserProfile> getCurrentUser() {
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
    @org.jetbrains.annotations.Nullable
    public java.lang.Object create(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.user.domain.model.UserProfile entity, @org.jetbrains.annotations.NotNull
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
    public java.lang.Object delete(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}