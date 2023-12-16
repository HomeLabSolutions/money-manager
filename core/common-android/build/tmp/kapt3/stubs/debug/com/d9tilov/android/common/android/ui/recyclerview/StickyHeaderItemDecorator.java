package com.d9tilov.android.common.android.ui.recyclerview;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\u001f\u0012\u0018\u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\u000f\u001a\u00020\u00102\b\u0010\r\u001a\u0004\u0018\u00010\u000eJ\u0012\u0010\u0011\u001a\u00020\u00102\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0002J\u0010\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\b\u0010\u0015\u001a\u00020\u0010H\u0002J\u001a\u0010\u0016\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\nH\u0002J\u0018\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\fH\u0002J \u0010\u001b\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\b\u0010\u001e\u001a\u00020\u0010H\u0002J\u0010\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\fH\u0002J\u0010\u0010\"\u001a\u00020\u00102\u0006\u0010#\u001a\u00020\nH\u0002R \u0010\u0004\u001a\u0014\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/d9tilov/android/common/android/ui/recyclerview/StickyHeaderItemDecorator;", "T", "", "Landroidx/recyclerview/widget/RecyclerView$ItemDecoration;", "adapter", "Lcom/d9tilov/android/common/android/ui/recyclerview/StickyAdapter;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "(Lcom/d9tilov/android/common/android/ui/recyclerview/StickyAdapter;)V", "currentStickyHolder", "currentStickyPosition", "", "lastViewOverlappedByHeader", "Landroid/view/View;", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "attachToRecyclerView", "", "destroyCallbacks", "drawHeader", "c", "Landroid/graphics/Canvas;", "fixLayoutSize", "getChildInContact", "parent", "contactPoint", "moveHeader", "nextHeader", "onDrawOver", "state", "Landroidx/recyclerview/widget/RecyclerView$State;", "setupCallbacks", "shouldMoveHeader", "", "viewOverlappedByHeader", "updateStickyHeader", "topChildPosition", "common-android_debug"})
public final class StickyHeaderItemDecorator<T extends java.lang.Object> extends androidx.recyclerview.widget.RecyclerView.ItemDecoration {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.common.android.ui.recyclerview.StickyAdapter<T, androidx.recyclerview.widget.RecyclerView.ViewHolder, androidx.recyclerview.widget.RecyclerView.ViewHolder> adapter = null;
    private int currentStickyPosition = androidx.recyclerview.widget.RecyclerView.NO_POSITION;
    @org.jetbrains.annotations.Nullable
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    @org.jetbrains.annotations.Nullable
    private androidx.recyclerview.widget.RecyclerView.ViewHolder currentStickyHolder;
    @org.jetbrains.annotations.Nullable
    private android.view.View lastViewOverlappedByHeader;
    
    public StickyHeaderItemDecorator(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.common.android.ui.recyclerview.StickyAdapter<T, androidx.recyclerview.widget.RecyclerView.ViewHolder, androidx.recyclerview.widget.RecyclerView.ViewHolder> adapter) {
        super();
    }
    
    public final void attachToRecyclerView(@org.jetbrains.annotations.Nullable
    androidx.recyclerview.widget.RecyclerView recyclerView) {
    }
    
    private final void setupCallbacks() {
    }
    
    private final void destroyCallbacks(androidx.recyclerview.widget.RecyclerView recyclerView) {
    }
    
    @java.lang.Override
    public void onDrawOver(@org.jetbrains.annotations.NotNull
    android.graphics.Canvas c, @org.jetbrains.annotations.NotNull
    androidx.recyclerview.widget.RecyclerView parent, @org.jetbrains.annotations.NotNull
    androidx.recyclerview.widget.RecyclerView.State state) {
    }
    
    private final boolean shouldMoveHeader(android.view.View viewOverlappedByHeader) {
        return false;
    }
    
    private final void updateStickyHeader(int topChildPosition) {
    }
    
    private final void drawHeader(android.graphics.Canvas c) {
    }
    
    private final void moveHeader(android.graphics.Canvas c, android.view.View nextHeader) {
    }
    
    private final android.view.View getChildInContact(androidx.recyclerview.widget.RecyclerView parent, int contactPoint) {
        return null;
    }
    
    private final void fixLayoutSize() {
    }
}