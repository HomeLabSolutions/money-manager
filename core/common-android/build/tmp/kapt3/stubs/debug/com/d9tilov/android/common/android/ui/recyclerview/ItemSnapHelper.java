package com.d9tilov.android.common.android.ui.recyclerview;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016J\u0018\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0014H\u0016J\u0018\u0010\u0015\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\bH\u0016J\u0012\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u0011\u001a\u00020\u0012H\u0014J\u0018\u0010\u001a\u001a\u00020\b2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u001c\u0010\u001b\u001a\u0004\u0018\u00010\u00142\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0014\u0010\u001c\u001a\u0004\u0018\u00010\u00142\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0016J\u0012\u0010\u0005\u001a\u00020\u00062\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/d9tilov/android/common/android/ui/recyclerview/ItemSnapHelper;", "Landroidx/recyclerview/widget/LinearSnapHelper;", "()V", "context", "Landroid/content/Context;", "helper", "Landroidx/recyclerview/widget/OrientationHelper;", "maxScrollDistance", "", "scroller", "Landroid/widget/Scroller;", "attachToRecyclerView", "", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "calculateDistanceToFinalSnap", "", "layoutManager", "Landroidx/recyclerview/widget/RecyclerView$LayoutManager;", "targetView", "Landroid/view/View;", "calculateScrollDistance", "velocityX", "velocityY", "createScroller", "Landroidx/recyclerview/widget/RecyclerView$SmoothScroller;", "distanceToStart", "findFirstView", "findSnapView", "Companion", "common-android_debug"})
public final class ItemSnapHelper extends androidx.recyclerview.widget.LinearSnapHelper {
    private static final float MILLISECONDS_PER_INCH = 100.0F;
    private static final int MAX_SCROLL_ON_FLING_DURATION_MS = 1000;
    @org.jetbrains.annotations.Nullable
    private android.content.Context context;
    @org.jetbrains.annotations.Nullable
    private androidx.recyclerview.widget.OrientationHelper helper;
    @org.jetbrains.annotations.Nullable
    private android.widget.Scroller scroller;
    private int maxScrollDistance = 0;
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.common.android.ui.recyclerview.ItemSnapHelper.Companion Companion = null;
    
    public ItemSnapHelper() {
        super();
    }
    
    @java.lang.Override
    public void attachToRecyclerView(@org.jetbrains.annotations.Nullable
    androidx.recyclerview.widget.RecyclerView recyclerView) {
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public android.view.View findSnapView(@org.jetbrains.annotations.Nullable
    androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public int[] calculateDistanceToFinalSnap(@org.jetbrains.annotations.NotNull
    androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager, @org.jetbrains.annotations.NotNull
    android.view.View targetView) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public int[] calculateScrollDistance(int velocityX, int velocityY) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    protected androidx.recyclerview.widget.RecyclerView.SmoothScroller createScroller(@org.jetbrains.annotations.NotNull
    androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager) {
        return null;
    }
    
    private final int distanceToStart(android.view.View targetView, androidx.recyclerview.widget.OrientationHelper helper) {
        return 0;
    }
    
    private final android.view.View findFirstView(androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager, androidx.recyclerview.widget.OrientationHelper helper) {
        return null;
    }
    
    private final androidx.recyclerview.widget.OrientationHelper helper(androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/d9tilov/android/common/android/ui/recyclerview/ItemSnapHelper$Companion;", "", "()V", "MAX_SCROLL_ON_FLING_DURATION_MS", "", "MILLISECONDS_PER_INCH", "", "common-android_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}