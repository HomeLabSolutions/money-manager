package com.d9tilov.android.incomeexpense.ui.vm;

import java.lang.System;

@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\'\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u0003B\u0005\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0011\u001a\u00020\u0012J \u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H&R\u001e\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u001e\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\fX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u0019"}, d2 = {"Lcom/d9tilov/android/incomeexpense/ui/vm/BaseIncomeExpenseViewModel;", "T", "Lcom/d9tilov/android/incomeexpense/navigation/BaseIncomeExpenseNavigator;", "Lcom/d9tilov/android/common_android/ui/base/BaseViewModel;", "()V", "categories", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/d9tilov/android/category/domain/model/Category;", "getCategories", "()Lkotlinx/coroutines/flow/StateFlow;", "transactions", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/paging/PagingData;", "Lcom/d9tilov/android/transaction/domain/model/BaseTransaction;", "getTransactions", "()Lkotlinx/coroutines/flow/Flow;", "openAllCategories", "", "saveTransaction", "category", "sum", "Ljava/math/BigDecimal;", "currencyCode", "", "incomeexpense_release"})
public abstract class BaseIncomeExpenseViewModel<T extends com.d9tilov.android.incomeexpense.navigation.BaseIncomeExpenseNavigator> extends com.d9tilov.android.common_android.ui.base.BaseViewModel<T> {
    
    public BaseIncomeExpenseViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.StateFlow<java.util.List<com.d9tilov.android.category.domain.model.Category>> getCategories();
    
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.d9tilov.android.transaction.domain.model.BaseTransaction>> getTransactions();
    
    public abstract void saveTransaction(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.Category category, @org.jetbrains.annotations.NotNull
    java.math.BigDecimal sum, @org.jetbrains.annotations.NotNull
    java.lang.String currencyCode);
    
    public final void openAllCategories() {
    }
}