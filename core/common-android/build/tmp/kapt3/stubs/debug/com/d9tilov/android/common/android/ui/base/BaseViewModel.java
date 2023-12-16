package com.d9tilov.android.common.android.ui.base;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\b&\u0018\u0000 \u001d*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003:\u0001\u001dB\u0005\u00a2\u0006\u0002\u0010\u0004J\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00070\u0013J\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\n0\u0015J\b\u0010\u0016\u001a\u00020\u0017H\u0014J\u000e\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u0007J\u001b\u0010\u001a\u001a\u00020\u00172\b\b\u0001\u0010\u001b\u001a\u00020\nH\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001cR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R*\u0010\f\u001a\u0004\u0018\u00018\u00002\b\u0010\u000b\u001a\u0004\u0018\u00018\u0000@FX\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\u0011\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001e"}, d2 = {"Lcom/d9tilov/android/common/android/ui/base/BaseViewModel;", "T", "Lcom/d9tilov/android/common/android/ui/base/BaseNavigator;", "Landroidx/lifecycle/ViewModel;", "()V", "loading", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "msg", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "", "value", "navigator", "getNavigator", "()Lcom/d9tilov/android/common/android/ui/base/BaseNavigator;", "setNavigator", "(Lcom/d9tilov/android/common/android/ui/base/BaseNavigator;)V", "Lcom/d9tilov/android/common/android/ui/base/BaseNavigator;", "getLoading", "Lkotlinx/coroutines/flow/StateFlow;", "getMsg", "Lkotlinx/coroutines/flow/SharedFlow;", "onNavigatorAttached", "", "setLoading", "show", "setMessage", "message", "(ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "common-android_debug"})
public abstract class BaseViewModel<T extends com.d9tilov.android.common.android.ui.base.BaseNavigator> extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.Nullable
    private T navigator;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.lang.Integer> msg = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> loading = null;
    public static final int DEFAULT_MESSAGE_ID = -1;
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.common.android.ui.base.BaseViewModel.Companion Companion = null;
    
    public BaseViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final T getNavigator() {
        return null;
    }
    
    public final void setNavigator(@org.jetbrains.annotations.Nullable
    T value) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object setMessage(@androidx.annotation.StringRes
    int message, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    public final void setLoading(boolean show) {
    }
    
    protected void onNavigatorAttached() {
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.SharedFlow<java.lang.Integer> getMsg() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getLoading() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/d9tilov/android/common/android/ui/base/BaseViewModel$Companion;", "", "()V", "DEFAULT_MESSAGE_ID", "", "common-android_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}