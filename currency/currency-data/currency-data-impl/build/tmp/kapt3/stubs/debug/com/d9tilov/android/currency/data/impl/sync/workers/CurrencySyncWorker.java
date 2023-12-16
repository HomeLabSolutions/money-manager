package com.d9tilov.android.currency.data.impl.sync.workers;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB#\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0011\u0010\t\u001a\u00020\nH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ\u0011\u0010\f\u001a\u00020\rH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u000f"}, d2 = {"Lcom/d9tilov/android/currency/data/impl/sync/workers/CurrencySyncWorker;", "Landroidx/work/CoroutineWorker;", "context", "Landroid/content/Context;", "workerParameters", "Landroidx/work/WorkerParameters;", "currencyInteractor", "Lcom/d9tilov/android/currency/domain/contract/CurrencyInteractor;", "(Landroid/content/Context;Landroidx/work/WorkerParameters;Lcom/d9tilov/android/currency/domain/contract/CurrencyInteractor;)V", "doWork", "Landroidx/work/ListenableWorker$Result;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getForegroundInfo", "Landroidx/work/ForegroundInfo;", "Companion", "currency-data-impl_debug"})
@androidx.hilt.work.HiltWorker
public final class CurrencySyncWorker extends androidx.work.CoroutineWorker {
    @org.jetbrains.annotations.NotNull
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.currency.domain.contract.CurrencyInteractor currencyInteractor = null;
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.currency.data.impl.sync.workers.CurrencySyncWorker.Companion Companion = null;
    
    @dagger.assisted.AssistedInject
    public CurrencySyncWorker(@dagger.assisted.Assisted
    @org.jetbrains.annotations.NotNull
    android.content.Context context, @dagger.assisted.Assisted
    @org.jetbrains.annotations.NotNull
    androidx.work.WorkerParameters workerParameters, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.domain.contract.CurrencyInteractor currencyInteractor) {
        super(null, null);
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object getForegroundInfo(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super androidx.work.ForegroundInfo> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object doWork(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super androidx.work.ListenableWorker.Result> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/d9tilov/android/currency/data/impl/sync/workers/CurrencySyncWorker$Companion;", "", "()V", "startUpSyncWork", "Landroidx/work/OneTimeWorkRequest;", "currency-data-impl_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * Expedited one time work to sync data on app startup
         */
        @org.jetbrains.annotations.NotNull
        public final androidx.work.OneTimeWorkRequest startUpSyncWork() {
            return null;
        }
    }
}