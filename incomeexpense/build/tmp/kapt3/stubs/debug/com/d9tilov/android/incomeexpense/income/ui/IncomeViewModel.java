package com.d9tilov.android.incomeexpense.income.ui;

import java.lang.System;

@dagger.hilt.android.lifecycle.HiltViewModel
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J \u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u000b2\u0006\u0010\u001f\u001a\u00020\u000f2\u0006\u0010 \u001a\u00020!H\u0016R \u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\tX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u001f\u0010\u000e\u001a\u0010\u0012\f\u0012\n \u0010*\u0004\u0018\u00010\u000f0\u000f0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u001f\u0010\u0012\u001a\u0010\u0012\f\u0012\n \u0010*\u0004\u0018\u00010\u000f0\u000f0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\rR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00170\u00160\u0015X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/d9tilov/android/incomeexpense/income/ui/IncomeViewModel;", "Lcom/d9tilov/android/incomeexpense/ui/vm/BaseIncomeExpenseViewModel;", "Lcom/d9tilov/android/incomeexpense/navigation/IncomeNavigator;", "categoryInteractor", "Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;", "transactionInteractor", "Lcom/d9tilov/android/transaction/domain/contract/TransactionInteractor;", "(Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;Lcom/d9tilov/android/transaction/domain/contract/TransactionInteractor;)V", "categories", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/d9tilov/android/category/domain/model/Category;", "getCategories", "()Lkotlinx/coroutines/flow/StateFlow;", "earnedInPeriod", "Ljava/math/BigDecimal;", "kotlin.jvm.PlatformType", "getEarnedInPeriod", "earnedInPeriodApprox", "getEarnedInPeriodApprox", "transactions", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/paging/PagingData;", "Lcom/d9tilov/android/transaction/domain/model/BaseTransaction;", "getTransactions", "()Lkotlinx/coroutines/flow/Flow;", "updateCurrencyExceptionHandler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "saveTransaction", "", "category", "sum", "currencyCode", "", "incomeexpense_debug"})
public final class IncomeViewModel extends com.d9tilov.android.incomeexpense.ui.vm.BaseIncomeExpenseViewModel<com.d9tilov.android.incomeexpense.navigation.IncomeNavigator> {
    private final com.d9tilov.android.transaction.domain.contract.TransactionInteractor transactionInteractor = null;
    private final kotlinx.coroutines.CoroutineExceptionHandler updateCurrencyExceptionHandler = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.math.BigDecimal> earnedInPeriod = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.math.BigDecimal> earnedInPeriodApprox = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.d9tilov.android.category.domain.model.Category>> categories = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.d9tilov.android.transaction.domain.model.BaseTransaction>> transactions = null;
    
    @javax.inject.Inject
    public IncomeViewModel(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.contract.CategoryInteractor categoryInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.transaction.domain.contract.TransactionInteractor transactionInteractor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.math.BigDecimal> getEarnedInPeriod() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.math.BigDecimal> getEarnedInPeriodApprox() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public kotlinx.coroutines.flow.StateFlow<java.util.List<com.d9tilov.android.category.domain.model.Category>> getCategories() {
        return null;
    }
    
    @java.lang.Override
    public void saveTransaction(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.Category category, @org.jetbrains.annotations.NotNull
    java.math.BigDecimal sum, @org.jetbrains.annotations.NotNull
    java.lang.String currencyCode) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.d9tilov.android.transaction.domain.model.BaseTransaction>> getTransactions() {
        return null;
    }
}