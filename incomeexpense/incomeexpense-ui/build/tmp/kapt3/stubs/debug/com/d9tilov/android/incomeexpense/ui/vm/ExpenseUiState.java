package com.d9tilov.android.incomeexpense.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\b\u0018\u0000 \u001d2\u00020\u0001:\u0001\u001dB7\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0014\b\u0002\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u0012\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\u0002\u0010\u000bJ\u000f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\u0015\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006H\u00c6\u0003J\u000b\u0010\u0014\u001a\u0004\u0018\u00010\nH\u00c6\u0003J;\u0010\u0015\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0014\b\u0002\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\nH\u00c6\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u001cH\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0013\u0010\t\u001a\u0004\u0018\u00010\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u001e"}, d2 = {"Lcom/d9tilov/android/incomeexpense/ui/vm/ExpenseUiState;", "", "expenseCategoryList", "", "Lcom/d9tilov/android/category/domain/model/Category;", "expenseTransactions", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/paging/PagingData;", "Lcom/d9tilov/android/transaction/domain/model/BaseTransaction;", "expenseInfo", "Lcom/d9tilov/android/incomeexpense/ui/vm/ExpenseInfo;", "(Ljava/util/List;Lkotlinx/coroutines/flow/Flow;Lcom/d9tilov/android/incomeexpense/ui/vm/ExpenseInfo;)V", "getExpenseCategoryList", "()Ljava/util/List;", "getExpenseInfo", "()Lcom/d9tilov/android/incomeexpense/ui/vm/ExpenseInfo;", "getExpenseTransactions", "()Lkotlinx/coroutines/flow/Flow;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "Companion", "incomeexpense-ui_debug"})
public final class ExpenseUiState {
    @org.jetbrains.annotations.NotNull
    private final java.util.List<com.d9tilov.android.category.domain.model.Category> expenseCategoryList = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.d9tilov.android.transaction.domain.model.BaseTransaction>> expenseTransactions = null;
    @org.jetbrains.annotations.Nullable
    private final com.d9tilov.android.incomeexpense.ui.vm.ExpenseInfo expenseInfo = null;
    @org.jetbrains.annotations.NotNull
    private static final com.d9tilov.android.incomeexpense.ui.vm.ExpenseUiState EMPTY = null;
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.incomeexpense.ui.vm.ExpenseUiState.Companion Companion = null;
    
    public ExpenseUiState(@org.jetbrains.annotations.NotNull
    java.util.List<com.d9tilov.android.category.domain.model.Category> expenseCategoryList, @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.d9tilov.android.transaction.domain.model.BaseTransaction>> expenseTransactions, @org.jetbrains.annotations.Nullable
    com.d9tilov.android.incomeexpense.ui.vm.ExpenseInfo expenseInfo) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.d9tilov.android.category.domain.model.Category> getExpenseCategoryList() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.d9tilov.android.transaction.domain.model.BaseTransaction>> getExpenseTransactions() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.d9tilov.android.incomeexpense.ui.vm.ExpenseInfo getExpenseInfo() {
        return null;
    }
    
    public ExpenseUiState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.List<com.d9tilov.android.category.domain.model.Category> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.d9tilov.android.transaction.domain.model.BaseTransaction>> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final com.d9tilov.android.incomeexpense.ui.vm.ExpenseInfo component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.incomeexpense.ui.vm.ExpenseUiState copy(@org.jetbrains.annotations.NotNull
    java.util.List<com.d9tilov.android.category.domain.model.Category> expenseCategoryList, @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.d9tilov.android.transaction.domain.model.BaseTransaction>> expenseTransactions, @org.jetbrains.annotations.Nullable
    com.d9tilov.android.incomeexpense.ui.vm.ExpenseInfo expenseInfo) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/d9tilov/android/incomeexpense/ui/vm/ExpenseUiState$Companion;", "", "()V", "EMPTY", "Lcom/d9tilov/android/incomeexpense/ui/vm/ExpenseUiState;", "getEMPTY", "()Lcom/d9tilov/android/incomeexpense/ui/vm/ExpenseUiState;", "incomeexpense-ui_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.incomeexpense.ui.vm.ExpenseUiState getEMPTY() {
            return null;
        }
    }
}