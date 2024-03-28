package com.d9tilov.android.incomeexpense.expense.ui;

import java.lang.System;

@dagger.hilt.android.lifecycle.HiltViewModel
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ \u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u00102\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020 H\u0016R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\u000eX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0019\u0010\u0013\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u00170\u000bX\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0015\u00a8\u0006!"}, d2 = {"Lcom/d9tilov/android/incomeexpense/expense/ui/ExpenseViewModel;", "Lcom/d9tilov/android/incomeexpense/ui/vm/BaseIncomeExpenseViewModel;", "Lcom/d9tilov/android/incomeexpense/navigation/ExpenseNavigator;", "categoryInteractor", "Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;", "billingInteractor", "Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;", "transactionInteractor", "Lcom/d9tilov/android/transaction/domain/contract/TransactionInteractor;", "(Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;Lcom/d9tilov/android/transaction/domain/contract/TransactionInteractor;)V", "_expenseSpendingInfo", "Lkotlinx/coroutines/flow/Flow;", "Lcom/d9tilov/android/transaction/domain/model/ExpenseInfoUiModel;", "categories", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/d9tilov/android/category/domain/model/Category;", "getCategories", "()Lkotlinx/coroutines/flow/StateFlow;", "expenseSpendingInfoPremium", "getExpenseSpendingInfoPremium", "()Lkotlinx/coroutines/flow/Flow;", "transactions", "Landroidx/paging/PagingData;", "Lcom/d9tilov/android/transaction/domain/model/BaseTransaction;", "getTransactions", "saveTransaction", "", "category", "sum", "Ljava/math/BigDecimal;", "currencyCode", "", "incomeexpense_debug"})
public final class ExpenseViewModel extends com.d9tilov.android.incomeexpense.ui.vm.BaseIncomeExpenseViewModel<com.d9tilov.android.incomeexpense.navigation.ExpenseNavigator> {
    private final com.d9tilov.android.transaction.domain.contract.TransactionInteractor transactionInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.d9tilov.android.transaction.domain.model.BaseTransaction>> transactions = null;
    private final kotlinx.coroutines.flow.Flow<com.d9tilov.android.transaction.domain.model.ExpenseInfoUiModel> _expenseSpendingInfo = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<com.d9tilov.android.transaction.domain.model.ExpenseInfoUiModel> expenseSpendingInfoPremium = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.d9tilov.android.category.domain.model.Category>> categories = null;
    
    @javax.inject.Inject
    public ExpenseViewModel(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.contract.CategoryInteractor categoryInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.billing.domain.contract.BillingInteractor billingInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.transaction.domain.contract.TransactionInteractor transactionInteractor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.d9tilov.android.transaction.domain.model.BaseTransaction>> getTransactions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<com.d9tilov.android.transaction.domain.model.ExpenseInfoUiModel> getExpenseSpendingInfoPremium() {
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
}