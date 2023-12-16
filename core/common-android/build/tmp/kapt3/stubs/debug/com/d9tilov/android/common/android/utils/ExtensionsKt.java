package com.d9tilov.android.common.android.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00008\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a>\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00030\u0001\"\u0004\b\u0000\u0010\u00022\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00030\u0001\u001aa\u0010\t\u001a\u0004\u0018\u0001H\n\"\b\b\u0000\u0010\u000b*\u00020\f\"\b\b\u0001\u0010\r*\u00020\f\"\b\b\u0002\u0010\n*\u00020\f2\b\u0010\u000e\u001a\u0004\u0018\u0001H\u000b2\b\u0010\u000f\u001a\u0004\u0018\u0001H\r2\u001a\u0010\u0010\u001a\u0016\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u0002H\r\u0012\u0006\u0012\u0004\u0018\u0001H\n0\u0011H\u0086\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012\u001a&\u0010\u0013\u001a\u00020\u0003*\u00020\u00142\u0014\b\u0004\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u00030\u0001H\u0086\b\u00f8\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\u0017"}, d2 = {"debounce", "Lkotlin/Function1;", "T", "", "waitMs", "", "scope", "Lkotlinx/coroutines/CoroutineScope;", "destinationFunction", "let2", "R", "T1", "", "T2", "p1", "p2", "block", "Lkotlin/Function2;", "(Ljava/lang/Object;Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "onChange", "Landroid/widget/EditText;", "cb", "", "common-android_debug"})
public final class ExtensionsKt {
    
    @org.jetbrains.annotations.Nullable
    public static final <T1 extends java.lang.Object, T2 extends java.lang.Object, R extends java.lang.Object>R let2(@org.jetbrains.annotations.Nullable
    T1 p1, @org.jetbrains.annotations.Nullable
    T2 p2, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super T1, ? super T2, ? extends R> block) {
        return null;
    }
    
    public static final void onChange(@org.jetbrains.annotations.NotNull
    android.widget.EditText $this$onChange, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> cb) {
    }
    
    @org.jetbrains.annotations.NotNull
    public static final <T extends java.lang.Object>kotlin.jvm.functions.Function1<T, kotlin.Unit> debounce(long waitMs, @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineScope scope, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super T, kotlin.Unit> destinationFunction) {
        return null;
    }
}