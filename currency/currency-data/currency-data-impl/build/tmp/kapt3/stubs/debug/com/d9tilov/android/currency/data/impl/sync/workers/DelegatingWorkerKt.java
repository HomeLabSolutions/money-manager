package com.d9tilov.android.currency.data.impl.sync.workers;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\u0014\u0010\u0002\u001a\u00020\u0003*\n\u0012\u0006\b\u0001\u0012\u00020\u00050\u0004H\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"WORKER_CLASS_NAME", "", "delegatedData", "Landroidx/work/Data;", "Lkotlin/reflect/KClass;", "Landroidx/work/CoroutineWorker;", "currency-data-impl_debug"})
public final class DelegatingWorkerKt {
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String WORKER_CLASS_NAME = "RouterWorkerDelegateClassName";
    
    /**
     * Adds metadata to a WorkRequest to identify what [CoroutineWorker] the [DelegatingWorker] should
     * delegate to
     */
    @org.jetbrains.annotations.NotNull
    public static final androidx.work.Data delegatedData(@org.jetbrains.annotations.NotNull
    kotlin.reflect.KClass<? extends androidx.work.CoroutineWorker> $this$delegatedData) {
        return null;
    }
}