package com.d9tilov.android.currency.data.impl.sync.initializers;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000&\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\n\u0010\b\u001a\u00020\t*\u00020\n\u001a\f\u0010\u000b\u001a\u00020\f*\u00020\nH\u0002\"\u0011\u0010\u0000\u001a\u00020\u00018F\u00a2\u0006\u0006\u001a\u0004\b\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\r"}, d2 = {"SyncConstraints", "Landroidx/work/Constraints;", "getSyncConstraints", "()Landroidx/work/Constraints;", "SyncNotificationChannelID", "", "SyncNotificationId", "", "syncForegroundInfo", "Landroidx/work/ForegroundInfo;", "Landroid/content/Context;", "syncWorkNotification", "Landroid/app/Notification;", "currency-data-impl_debug"})
public final class SyncWorkHelpersKt {
    private static final int SyncNotificationId = 0;
    @org.jetbrains.annotations.NotNull
    private static final java.lang.String SyncNotificationChannelID = "SyncNotificationChannel";
    
    @org.jetbrains.annotations.NotNull
    public static final androidx.work.Constraints getSyncConstraints() {
        return null;
    }
    
    /**
     * Foreground information for sync on lower API levels when sync workers are being
     * run with a foreground service
     */
    @org.jetbrains.annotations.NotNull
    public static final androidx.work.ForegroundInfo syncForegroundInfo(@org.jetbrains.annotations.NotNull
    android.content.Context $this$syncForegroundInfo) {
        return null;
    }
    
    /**
     * Notification displayed on lower API levels when sync workers are being
     * run with a foreground service
     */
    private static final android.app.Notification syncWorkNotification(android.content.Context $this$syncWorkNotification) {
        return null;
    }
}