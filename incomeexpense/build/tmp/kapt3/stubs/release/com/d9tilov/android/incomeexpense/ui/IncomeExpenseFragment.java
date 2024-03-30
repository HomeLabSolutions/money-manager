package com.d9tilov.android.incomeexpense.ui;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u00022\u00020\u0004B\u0005\u00a2\u0006\u0002\u0010\u0005J\u0006\u0010\u001e\u001a\u00020\u001fJ\u0006\u0010 \u001a\u00020!J\u000e\u0010\"\u001a\b\u0012\u0004\u0012\u00020$0#H\u0002J\b\u0010%\u001a\u00020\u0000H\u0016J\u0006\u0010&\u001a\u00020\'J\u0006\u0010(\u001a\u00020\u0012J\b\u0010)\u001a\u00020\u0012H\u0016J\b\u0010*\u001a\u00020\u001fH\u0016J\u0012\u0010+\u001a\u00020\u001f2\b\u0010,\u001a\u0004\u0018\u00010!H\u0002J\u001a\u0010-\u001a\u00020\u001f2\u0006\u0010.\u001a\u00020\b2\b\u0010/\u001a\u0004\u0018\u000100H\u0016J\u0006\u00101\u001a\u00020\u001fJ\u0006\u00102\u001a\u00020\u001fR\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\t\u001a\u00020\n8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u0012@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001e\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0011\u001a\u00020\u0012@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0014R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0018\u001a\u00020\u00198TX\u0094\u0084\u0002\u00a2\u0006\f\n\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001a\u0010\u001b\u00a8\u00063"}, d2 = {"Lcom/d9tilov/android/incomeexpense/ui/IncomeExpenseFragment;", "Lcom/d9tilov/android/common_android/ui/base/BaseFragment;", "Lcom/d9tilov/android/incomeexpense/navigation/IncomeExpenseNavigator;", "Lcom/d9tilov/android/incomeexpense/databinding/FragmentIncomeExpenseBinding;", "Lcom/d9tilov/android/core/events/OnBackPressed;", "()V", "commonGroup", "", "Landroid/view/View;", "firebaseAnalytics", "Lcom/google/firebase/analytics/FirebaseAnalytics;", "getFirebaseAnalytics", "()Lcom/google/firebase/analytics/FirebaseAnalytics;", "setFirebaseAnalytics", "(Lcom/google/firebase/analytics/FirebaseAnalytics;)V", "incomeExpenseAdapter", "Lcom/d9tilov/android/incomeexpense/ui/adapter/IncomeExpenseAdapter;", "<set-?>", "", "isKeyboardOpen", "()Z", "isPremium", "pageIndex", "", "viewModel", "Lcom/d9tilov/android/incomeexpense/ui/vm/IncomeExpenseViewModel;", "getViewModel", "()Lcom/d9tilov/android/incomeexpense/ui/vm/IncomeExpenseViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "closeKeyboard", "", "getCurrencyCode", "", "getFragmentsInViewPager", "", "Landroidx/fragment/app/Fragment;", "getNavigator", "getSum", "Ljava/math/BigDecimal;", "isKeyboardOpened", "onBackPressed", "onDestroyView", "onHandleInput", "str", "onViewCreated", "view", "savedInstanceState", "Landroid/os/Bundle;", "openKeyboard", "resetSum", "incomeexpense_release"})
@dagger.hilt.android.AndroidEntryPoint
public final class IncomeExpenseFragment extends com.d9tilov.android.common_android.ui.base.BaseFragment<com.d9tilov.android.incomeexpense.navigation.IncomeExpenseNavigator, com.d9tilov.android.incomeexpense.databinding.FragmentIncomeExpenseBinding> implements com.d9tilov.android.incomeexpense.navigation.IncomeExpenseNavigator, com.d9tilov.android.core.events.OnBackPressed {
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy viewModel$delegate = null;
    private com.d9tilov.android.incomeexpense.ui.adapter.IncomeExpenseAdapter incomeExpenseAdapter;
    private int pageIndex = -1;
    private final java.util.List<android.view.View> commonGroup = null;
    private boolean isKeyboardOpen = true;
    private boolean isPremium = false;
    @javax.inject.Inject
    public com.google.firebase.analytics.FirebaseAnalytics firebaseAnalytics;
    
    public IncomeExpenseFragment() {
        super(null, 0);
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.d9tilov.android.incomeexpense.ui.IncomeExpenseFragment getNavigator() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    protected com.d9tilov.android.incomeexpense.ui.vm.IncomeExpenseViewModel getViewModel() {
        return null;
    }
    
    public final boolean isKeyboardOpen() {
        return false;
    }
    
    public final boolean isPremium() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.google.firebase.analytics.FirebaseAnalytics getFirebaseAnalytics() {
        return null;
    }
    
    public final void setFirebaseAnalytics(@org.jetbrains.annotations.NotNull
    com.google.firebase.analytics.FirebaseAnalytics p0) {
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void onHandleInput(java.lang.String str) {
    }
    
    public final void openKeyboard() {
    }
    
    public final void closeKeyboard() {
    }
    
    public final boolean isKeyboardOpened() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.math.BigDecimal getSum() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getCurrencyCode() {
        return null;
    }
    
    public final void resetSum() {
    }
    
    private final java.util.List<androidx.fragment.app.Fragment> getFragmentsInViewPager() {
        return null;
    }
    
    @java.lang.Override
    public boolean onBackPressed() {
        return false;
    }
    
    @java.lang.Override
    public void onDestroyView() {
    }
}