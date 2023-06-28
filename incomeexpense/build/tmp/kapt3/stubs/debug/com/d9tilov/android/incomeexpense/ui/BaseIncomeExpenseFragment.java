package com.d9tilov.android.incomeexpense.ui;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u00a4\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\b\'\u0018\u0000 V*\b\b\u0000\u0010\u0001*\u00020\u0002*\b\b\u0001\u0010\u0003*\u00020\u00042\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00030\u00052\u00020\u00062\u00020\u00022\u00020\u0007:\u0001VB;\u0012*\u0010\b\u001a&\u0012\u0004\u0012\u00020\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00028\u00010\tj\b\u0012\u0004\u0012\u00028\u0001`\r\u0012\b\b\u0001\u0010\u000e\u001a\u00020\u000f\u00a2\u0006\u0002\u0010\u0010J\b\u00108\u001a\u000209H\u0004J\b\u0010:\u001a\u00020;H\u0004J\b\u0010<\u001a\u00020=H$J\b\u0010>\u001a\u00020?H\u0002J\b\u0010@\u001a\u00020?H$J\b\u0010A\u001a\u00020?H$J\b\u0010B\u001a\u00020?H$J\b\u0010C\u001a\u00020\fH\u0004J\b\u0010D\u001a\u00020\fH\u0002J\b\u0010E\u001a\u00020?H\u0016J\u0010\u0010F\u001a\u00020?2\u0006\u0010G\u001a\u00020\fH\u0016J\u001a\u0010H\u001a\u00020?2\u0006\u0010I\u001a\u00020J2\b\u0010K\u001a\u0004\u0018\u00010LH\u0016J\u0010\u0010M\u001a\u00020?2\u0006\u0010N\u001a\u00020OH\u0002J\b\u0010P\u001a\u00020?H\u0002J\u0010\u0010Q\u001a\u00020?2\u0006\u0010R\u001a\u00020SH$J\b\u0010T\u001a\u00020?H\u0016J\b\u0010U\u001a\u00020?H\u0002R\u0014\u0010\u0011\u001a\u00020\u0012X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u0094\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001c\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u0094\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u001c\u0010!\u001a\u0004\u0018\u00010\"X\u0094\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b#\u0010$\"\u0004\b%\u0010&R\u000e\u0010\'\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010(\u001a\u00020)X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010+R\u001c\u0010,\u001a\u0004\u0018\u00010-X\u0094\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b.\u0010/\"\u0004\b0\u00101R\u001c\u00102\u001a\u0004\u0018\u00010\u0016X\u0094\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\u0018\"\u0004\b4\u0010\u001aR\u001c\u00105\u001a\u0004\u0018\u00010\"X\u0094\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b6\u0010$\"\u0004\b7\u0010&\u00a8\u0006W"}, d2 = {"Lcom/d9tilov/android/incomeexpense/ui/BaseIncomeExpenseFragment;", "N", "Lcom/d9tilov/android/incomeexpense/navigation/BaseIncomeExpenseNavigator;", "VB", "Landroidx/viewbinding/ViewBinding;", "Lcom/d9tilov/android/common_android/ui/base/BaseFragment;", "Lcom/d9tilov/android/core/events/OnDialogDismissListener;", "Lcom/d9tilov/android/incomeexpense/ui/listeners/OnIncomeExpenseListener;", "inflate", "Lkotlin/Function3;", "Landroid/view/LayoutInflater;", "Landroid/view/ViewGroup;", "", "Lcom/d9tilov/android/common_android/ui/base/Inflate;", "layoutId", "", "(Lkotlin/jvm/functions/Function3;I)V", "categoryAdapter", "Lcom/d9tilov/android/category/ui/recycler/CategoryAdapter;", "getCategoryAdapter", "()Lcom/d9tilov/android/category/ui/recycler/CategoryAdapter;", "categoryRvList", "Landroidx/recyclerview/widget/RecyclerView;", "getCategoryRvList", "()Landroidx/recyclerview/widget/RecyclerView;", "setCategoryRvList", "(Landroidx/recyclerview/widget/RecyclerView;)V", "emptyViewStub", "Lcom/d9tilov/android/designsystem/databinding/LayoutEmptyListPlaceholderBinding;", "getEmptyViewStub", "()Lcom/d9tilov/android/designsystem/databinding/LayoutEmptyListPlaceholderBinding;", "setEmptyViewStub", "(Lcom/d9tilov/android/designsystem/databinding/LayoutEmptyListPlaceholderBinding;)V", "infoLayout", "Landroidx/constraintlayout/widget/ConstraintLayout;", "getInfoLayout", "()Landroidx/constraintlayout/widget/ConstraintLayout;", "setInfoLayout", "(Landroidx/constraintlayout/widget/ConstraintLayout;)V", "isTransactionDataEmpty", "transactionAdapter", "Lcom/d9tilov/android/transaction/ui/TransactionAdapter;", "getTransactionAdapter", "()Lcom/d9tilov/android/transaction/ui/TransactionAdapter;", "transactionBtnAdd", "Lcom/google/android/material/floatingactionbutton/FloatingActionButton;", "getTransactionBtnAdd", "()Lcom/google/android/material/floatingactionbutton/FloatingActionButton;", "setTransactionBtnAdd", "(Lcom/google/android/material/floatingactionbutton/FloatingActionButton;)V", "transactionRvList", "getTransactionRvList", "setTransactionRvList", "transactionsLayout", "getTransactionsLayout", "setTransactionsLayout", "getCurrencyCode", "", "getSum", "Ljava/math/BigDecimal;", "getType", "Lcom/d9tilov/android/core/model/TransactionType;", "hideViewStub", "", "initCategoryRecyclerView", "initTransactionsRecyclerView", "initViews", "isKeyboardOpen", "isPremium", "onDismiss", "onKeyboardShown", "show", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "openRemoveConfirmationDialog", "transaction", "Lcom/d9tilov/android/transaction/domain/model/Transaction;", "resetMainSum", "saveTransaction", "category", "Lcom/d9tilov/android/category/domain/model/Category;", "showEmptySumError", "showViewStub", "Companion", "incomeexpense_debug"})
public abstract class BaseIncomeExpenseFragment<N extends com.d9tilov.android.incomeexpense.navigation.BaseIncomeExpenseNavigator, VB extends androidx.viewbinding.ViewBinding> extends com.d9tilov.android.common_android.ui.base.BaseFragment<N, VB> implements com.d9tilov.android.core.events.OnDialogDismissListener, com.d9tilov.android.incomeexpense.navigation.BaseIncomeExpenseNavigator, com.d9tilov.android.incomeexpense.ui.listeners.OnIncomeExpenseListener {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.category.ui.recycler.CategoryAdapter categoryAdapter = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.transaction.ui.TransactionAdapter transactionAdapter = null;
    private boolean isTransactionDataEmpty = false;
    @org.jetbrains.annotations.Nullable
    private com.d9tilov.android.designsystem.databinding.LayoutEmptyListPlaceholderBinding emptyViewStub;
    @org.jetbrains.annotations.Nullable
    private androidx.recyclerview.widget.RecyclerView categoryRvList;
    @org.jetbrains.annotations.Nullable
    private androidx.recyclerview.widget.RecyclerView transactionRvList;
    @org.jetbrains.annotations.Nullable
    private com.google.android.material.floatingactionbutton.FloatingActionButton transactionBtnAdd;
    @org.jetbrains.annotations.Nullable
    private androidx.constraintlayout.widget.ConstraintLayout infoLayout;
    @org.jetbrains.annotations.Nullable
    private androidx.constraintlayout.widget.ConstraintLayout transactionsLayout;
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.incomeexpense.ui.BaseIncomeExpenseFragment.Companion Companion = null;
    public static final int SPAN_COUNT = 2;
    public static final int TABLET_SPAN_COUNT = 1;
    
    public BaseIncomeExpenseFragment(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function3<? super android.view.LayoutInflater, ? super android.view.ViewGroup, ? super java.lang.Boolean, ? extends VB> inflate, @androidx.annotation.LayoutRes
    int layoutId) {
        super(null, 0);
    }
    
    @org.jetbrains.annotations.NotNull
    protected final com.d9tilov.android.category.ui.recycler.CategoryAdapter getCategoryAdapter() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    protected final com.d9tilov.android.transaction.ui.TransactionAdapter getTransactionAdapter() {
        return null;
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void showViewStub() {
    }
    
    private final void hideViewStub() {
    }
    
    private final void openRemoveConfirmationDialog(com.d9tilov.android.transaction.domain.model.Transaction transaction) {
    }
    
    @java.lang.Override
    public void showEmptySumError() {
    }
    
    @java.lang.Override
    public void onDismiss() {
    }
    
    @java.lang.Override
    public void onKeyboardShown(boolean show) {
    }
    
    private final boolean isPremium() {
        return false;
    }
    
    protected final boolean isKeyboardOpen() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull
    protected final java.math.BigDecimal getSum() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    protected final java.lang.String getCurrencyCode() {
        return null;
    }
    
    private final void resetMainSum() {
    }
    
    @org.jetbrains.annotations.Nullable
    protected com.d9tilov.android.designsystem.databinding.LayoutEmptyListPlaceholderBinding getEmptyViewStub() {
        return null;
    }
    
    protected void setEmptyViewStub(@org.jetbrains.annotations.Nullable
    com.d9tilov.android.designsystem.databinding.LayoutEmptyListPlaceholderBinding p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    protected androidx.recyclerview.widget.RecyclerView getCategoryRvList() {
        return null;
    }
    
    protected void setCategoryRvList(@org.jetbrains.annotations.Nullable
    androidx.recyclerview.widget.RecyclerView p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    protected androidx.recyclerview.widget.RecyclerView getTransactionRvList() {
        return null;
    }
    
    protected void setTransactionRvList(@org.jetbrains.annotations.Nullable
    androidx.recyclerview.widget.RecyclerView p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    protected com.google.android.material.floatingactionbutton.FloatingActionButton getTransactionBtnAdd() {
        return null;
    }
    
    protected void setTransactionBtnAdd(@org.jetbrains.annotations.Nullable
    com.google.android.material.floatingactionbutton.FloatingActionButton p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    protected androidx.constraintlayout.widget.ConstraintLayout getInfoLayout() {
        return null;
    }
    
    protected void setInfoLayout(@org.jetbrains.annotations.Nullable
    androidx.constraintlayout.widget.ConstraintLayout p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    protected androidx.constraintlayout.widget.ConstraintLayout getTransactionsLayout() {
        return null;
    }
    
    protected void setTransactionsLayout(@org.jetbrains.annotations.Nullable
    androidx.constraintlayout.widget.ConstraintLayout p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    protected abstract com.d9tilov.android.core.model.TransactionType getType();
    
    protected abstract void initViews();
    
    protected abstract void initCategoryRecyclerView();
    
    protected abstract void initTransactionsRecyclerView();
    
    protected abstract void saveTransaction(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.Category category);
    
    @kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/d9tilov/android/incomeexpense/ui/BaseIncomeExpenseFragment$Companion;", "", "()V", "SPAN_COUNT", "", "TABLET_SPAN_COUNT", "incomeexpense_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}