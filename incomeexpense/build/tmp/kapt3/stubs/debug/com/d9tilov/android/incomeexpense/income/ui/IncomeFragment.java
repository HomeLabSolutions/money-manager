package com.d9tilov.android.incomeexpense.income.ui;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 &2\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u00012\u00020\u0002:\u0001&B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0016\u001a\u00020\u0000H\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0014J\b\u0010\u0019\u001a\u00020\u001aH\u0014J\b\u0010\u001b\u001a\u00020\u001aH\u0014J\b\u0010\u001c\u001a\u00020\u001aH\u0014J\u001a\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\b\u0010\"\u001a\u00020\u001aH\u0016J\u0010\u0010#\u001a\u00020\u001a2\u0006\u0010$\u001a\u00020%H\u0014R\u001e\u0010\u0005\u001a\u00020\u00068\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001d\u0010\u000b\u001a\u0004\u0018\u00010\f8TX\u0094\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000f\u0010\u0010\u001a\u0004\b\r\u0010\u000eR\u001b\u0010\u0011\u001a\u00020\u00128TX\u0094\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0015\u0010\u0010\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006\'"}, d2 = {"Lcom/d9tilov/android/incomeexpense/income/ui/IncomeFragment;", "Lcom/d9tilov/android/incomeexpense/ui/BaseIncomeExpenseFragment;", "Lcom/d9tilov/android/incomeexpense/navigation/IncomeNavigator;", "Lcom/d9tilov/android/incomeexpense/databinding/FragmentIncomeBinding;", "()V", "firebaseAnalytics", "Lcom/google/firebase/analytics/FirebaseAnalytics;", "getFirebaseAnalytics", "()Lcom/google/firebase/analytics/FirebaseAnalytics;", "setFirebaseAnalytics", "(Lcom/google/firebase/analytics/FirebaseAnalytics;)V", "snackBarAnchorView", "Landroidx/recyclerview/widget/RecyclerView;", "getSnackBarAnchorView", "()Landroidx/recyclerview/widget/RecyclerView;", "snackBarAnchorView$delegate", "Lkotlin/Lazy;", "viewModel", "Lcom/d9tilov/android/incomeexpense/income/ui/IncomeViewModel;", "getViewModel", "()Lcom/d9tilov/android/incomeexpense/income/ui/IncomeViewModel;", "viewModel$delegate", "getNavigator", "getType", "Lcom/d9tilov/android/core/model/TransactionType$INCOME;", "initCategoryRecyclerView", "", "initTransactionsRecyclerView", "initViews", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "openCategoriesScreen", "saveTransaction", "category", "Lcom/d9tilov/android/category/domain/model/Category;", "Companion", "incomeexpense_debug"})
@dagger.hilt.android.AndroidEntryPoint
public final class IncomeFragment extends com.d9tilov.android.incomeexpense.ui.BaseIncomeExpenseFragment<com.d9tilov.android.incomeexpense.navigation.IncomeNavigator, com.d9tilov.android.incomeexpense.databinding.FragmentIncomeBinding> implements com.d9tilov.android.incomeexpense.navigation.IncomeNavigator {
    @org.jetbrains.annotations.NotNull
    private final kotlin.Lazy viewModel$delegate = null;
    @org.jetbrains.annotations.Nullable
    private final kotlin.Lazy snackBarAnchorView$delegate = null;
    @javax.inject.Inject
    public com.google.firebase.analytics.FirebaseAnalytics firebaseAnalytics;
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.incomeexpense.income.ui.IncomeFragment.Companion Companion = null;
    public static final long FRAGMENT_ID = 1L;
    
    public IncomeFragment() {
        super(null, 0);
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public com.d9tilov.android.incomeexpense.income.ui.IncomeFragment getNavigator() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    protected com.d9tilov.android.incomeexpense.income.ui.IncomeViewModel getViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    @java.lang.Override
    protected androidx.recyclerview.widget.RecyclerView getSnackBarAnchorView() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.google.firebase.analytics.FirebaseAnalytics getFirebaseAnalytics() {
        return null;
    }
    
    public final void setFirebaseAnalytics(@org.jetbrains.annotations.NotNull
    com.google.firebase.analytics.FirebaseAnalytics p0) {
    }
    
    @java.lang.Override
    protected void initViews() {
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override
    protected void initCategoryRecyclerView() {
    }
    
    @java.lang.Override
    protected void initTransactionsRecyclerView() {
    }
    
    @java.lang.Override
    public void openCategoriesScreen() {
    }
    
    @java.lang.Override
    protected void saveTransaction(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.Category category) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    protected com.d9tilov.android.core.model.TransactionType.INCOME getType() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/d9tilov/android/incomeexpense/income/ui/IncomeFragment$Companion;", "", "()V", "FRAGMENT_ID", "", "newInstance", "Lcom/d9tilov/android/incomeexpense/income/ui/IncomeFragment;", "incomeexpense_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.incomeexpense.income.ui.IncomeFragment newInstance() {
            return null;
        }
    }
}