package com.d9tilov.android.budget.data.impl;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u001f\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0019\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\rJ\u0019\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u0010H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00100\u0013H\u0016J\u0019\u0010\u0014\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u0010H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0015"}, d2 = {"Lcom/d9tilov/android/budget/data/impl/BudgetLocalSource;", "Lcom/d9tilov/android/budget/data/contract/BudgetSource;", "dispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "preferencesStore", "Lcom/d9tilov/android/datastore/PreferencesStore;", "budgetDao", "Lcom/d9tilov/android/database/dao/BudgetDao;", "(Lkotlinx/coroutines/CoroutineDispatcher;Lcom/d9tilov/android/datastore/PreferencesStore;Lcom/d9tilov/android/database/dao/BudgetDao;)V", "createIfNeeded", "", "currencyCode", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "budgetData", "Lcom/d9tilov/android/budget/domain/model/BudgetData;", "(Lcom/d9tilov/android/budget/domain/model/BudgetData;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "get", "Lkotlinx/coroutines/flow/Flow;", "update", "budget-data-impl_debug"})
public final class BudgetLocalSource implements com.d9tilov.android.budget.data.contract.BudgetSource {
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.CoroutineDispatcher dispatcher = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.datastore.PreferencesStore preferencesStore = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.database.dao.BudgetDao budgetDao = null;
    
    public BudgetLocalSource(@com.d9tilov.android.network.dispatchers.Dispatcher(moneyManagerDispatcher = com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers.IO)
    @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineDispatcher dispatcher, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.datastore.PreferencesStore preferencesStore, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.dao.BudgetDao budgetDao) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object createIfNeeded(@org.jetbrains.annotations.NotNull
    java.lang.String currencyCode, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<com.d9tilov.android.budget.domain.model.BudgetData> get() {
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