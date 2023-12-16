package com.d9tilov.android.common.android.ui.base;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b&\u0018\u0000 **\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003:\u0001*B\u0005\u00a2\u0006\u0002\u0010\u0004J\n\u0010\u0017\u001a\u0004\u0018\u00010\u0018H\u0004J\u0006\u0010\u0019\u001a\u00020\u001aJ\u0012\u0010\u001b\u001a\u00020\u001a2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0014J\u0010\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!H\u0016J\r\u0010\"\u001a\u00028\u0000H&\u00a2\u0006\u0002\u0010\u0013J\u0006\u0010#\u001a\u00020\u001aJ$\u0010$\u001a\u00020\u001a2\u0006\u0010%\u001a\u00020&2\b\b\u0003\u0010\'\u001a\u00020\f2\n\b\u0002\u0010(\u001a\u0004\u0018\u00010)R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\u000b\u001a\u00020\fX\u0094D\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0011\u001a\u0004\u0018\u00018\u0000X\u0084\u000e\u00a2\u0006\u0010\n\u0002\u0010\u0016\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015\u00a8\u0006+"}, d2 = {"Lcom/d9tilov/android/common/android/ui/base/BaseActivity;", "T", "Landroidx/viewbinding/ViewBinding;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "contentLayout", "Landroid/view/ViewGroup;", "getContentLayout", "()Landroid/view/ViewGroup;", "contentLayout$delegate", "Lkotlin/Lazy;", "navHostFragmentId", "", "getNavHostFragmentId", "()I", "progress", "Landroid/widget/ProgressBar;", "viewBinding", "getViewBinding", "()Landroidx/viewbinding/ViewBinding;", "setViewBinding", "(Landroidx/viewbinding/ViewBinding;)V", "Landroidx/viewbinding/ViewBinding;", "getCurrentFragment", "Landroidx/fragment/app/Fragment;", "hideLoading", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onOptionsItemSelected", "", "item", "Landroid/view/MenuItem;", "performDataBinding", "showLoading", "showSnackBar", "text", "", "backgroundTint", "anchorView", "Landroid/view/View;", "Companion", "common-android_debug"})
public abstract class BaseActivity<T extends androidx.viewbinding.ViewBinding> extends androidx.appcompat.app.AppCompatActivity {
    private final int navHostFragmentId = -1;
    @org.jetbrains.annotations.Nullable
    private T viewBinding;
    @org.jetbrains.annotations.Nullable
    private android.widget.ProgressBar progress;
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy contentLayout$delegate = null;
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.common.android.ui.base.BaseActivity.Companion Companion = null;
    
    public BaseActivity() {
        super();
    }
    
    protected int getNavHostFragmentId() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable
    protected final T getViewBinding() {
        return null;
    }
    
    protected final void setViewBinding(@org.jetbrains.annotations.Nullable
    T p0) {
    }
    
    private final android.view.ViewGroup getContentLayout() {
        return null;
    }
    
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.NotNull
    public abstract T performDataBinding();
    
    public final void showLoading() {
    }
    
    public final void hideLoading() {
    }
    
    @java.lang.Override
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull
    android.view.MenuItem item) {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable
    protected final androidx.fragment.app.Fragment getCurrentFragment() {
        return null;
    }
    
    public final void showSnackBar(@org.jetbrains.annotations.NotNull
    java.lang.String text, @androidx.annotation.ColorInt
    int backgroundTint, @org.jetbrains.annotations.Nullable
    android.view.View anchorView) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0003"}, d2 = {"Lcom/d9tilov/android/common/android/ui/base/BaseActivity$Companion;", "", "()V", "common-android_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}