package com.d9tilov.android.budget.data.impl;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0019\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ\u0019\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\rJ\u000e\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\f0\u000fH\u0016J\u0019\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\fH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0011"}, d2 = {"Lcom/d9tilov/android/budget/data/impl/BudgetDataRepo;", "Lcom/d9tilov/android/budget/domain/contract/BudgetRepo;", "budgetSource", "Lcom/d9tilov/android/budget/data/contract/BudgetSource;", "(Lcom/d9tilov/android/budget/data/contract/BudgetSource;)V", "create", "", "currencyCode", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "budgetData", "Lcom/d9tilov/android/budget/domain/model/BudgetData;", "(Lcom/d9tilov/android/budget/domain/model/BudgetData;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "get", "Lkotlinx/coroutines/flow/Flow;", "update", "budget-data-impl_debug"})
public final class BudgetDataRepo implements com.d9tilov.android.budget.domain.contract.BudgetRepo {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.budget.data.contract.BudgetSource budgetSource = null;
    
    public BudgetDataRepo(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.budget.data.contract.BudgetSource budgetSource) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<com.d9tilov.android.budget.domain.model.BudgetData> get() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object create(@org.jetbrains.annotations.NotNull
    java.lang.String currencyCode, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object update(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.budget.domain.model.BudgetData budgetData, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object delete(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.budget.domain.model.BudgetData budgetData, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}