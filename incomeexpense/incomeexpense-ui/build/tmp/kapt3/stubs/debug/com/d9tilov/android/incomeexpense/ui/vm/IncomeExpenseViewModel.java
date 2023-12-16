package com.d9tilov.android.incomeexpense.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0088\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u000e\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eJ\u000e\u0010\u001f\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020!J\u000e\u0010\"\u001a\u00020\u001c2\u0006\u0010#\u001a\u00020$J(\u0010%\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020(0\'0&2\u0012\u0010)\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020$0\'0&H\u0002J\u000e\u0010*\u001a\u00020\u001c2\u0006\u0010+\u001a\u00020,J\u000e\u0010-\u001a\u00020\u001c2\u0006\u0010.\u001a\u00020/R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\r0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00120\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001a\u00a8\u00060"}, d2 = {"Lcom/d9tilov/android/incomeexpense/ui/vm/IncomeExpenseViewModel;", "Landroidx/lifecycle/ViewModel;", "billingInteractor", "Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;", "currencyInteractor", "Lcom/d9tilov/android/currency/domain/contract/CurrencyInteractor;", "categoryInteractor", "Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;", "transactionInteractor", "Lcom/d9tilov/android/transaction/domain/contract/TransactionInteractor;", "(Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;Lcom/d9tilov/android/currency/domain/contract/CurrencyInteractor;Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;Lcom/d9tilov/android/transaction/domain/contract/TransactionInteractor;)V", "_errorMessage", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "", "_mainCurrency", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/d9tilov/android/currency/domain/model/CurrencyMetaData;", "_uiState", "Lcom/d9tilov/android/incomeexpense/ui/vm/IncomeExpenseUiState;", "errorMessage", "Lkotlinx/coroutines/flow/SharedFlow;", "getErrorMessage", "()Lkotlinx/coroutines/flow/SharedFlow;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "addNumber", "", "btn", "Lcom/d9tilov/android/core/utils/KeyPress;", "addTransaction", "categoryId", "", "deleteTransaction", "transaction", "Lcom/d9tilov/android/transaction/domain/model/Transaction;", "mapWithStickyHeaders", "Lkotlinx/coroutines/flow/Flow;", "Landroidx/paging/PagingData;", "Lcom/d9tilov/android/transaction/domain/model/BaseTransaction;", "flow", "updateCurrencyCode", "code", "", "updateMode", "mode", "Lcom/d9tilov/android/incomeexpense/ui/vm/EditMode;", "incomeexpense-ui_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class IncomeExpenseViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.currency.domain.contract.CurrencyInteractor currencyInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.category.domain.contract.CategoryInteractor categoryInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.transaction.domain.contract.TransactionInteractor transactionInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.d9tilov.android.incomeexpense.ui.vm.IncomeExpenseUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.incomeexpense.ui.vm.IncomeExpenseUiState> uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.d9tilov.android.currency.domain.model.CurrencyMetaData> _mainCurrency = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableSharedFlow<java.lang.Integer> _errorMessage = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.SharedFlow<java.lang.Integer> errorMessage = null;
    
    @javax.inject.Inject
    public IncomeExpenseViewModel(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.billing.domain.contract.BillingInteractor billingInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.domain.contract.CurrencyInteractor currencyInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.contract.CategoryInteractor categoryInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.transaction.domain.contract.TransactionInteractor transactionInteractor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.incomeexpense.ui.vm.IncomeExpenseUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.SharedFlow<java.lang.Integer> getErrorMessage() {
        return null;
    }
    
    public final void addNumber(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.utils.KeyPress btn) {
    }
    
    public final void addTransaction(long categoryId) {
    }
    
    public final void updateCurrencyCode(@org.jetbrains.annotations.NotNull
    java.lang.String code) {
    }
    
    public final void updateMode(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.incomeexpense.ui.vm.EditMode mode) {
    }
    
    public final void deleteTransaction(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.transaction.domain.model.Transaction transaction) {
    }
    
    private final kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.d9tilov.android.transaction.domain.model.BaseTransaction>> mapWithStickyHeaders(kotlinx.coroutines.flow.Flow<androidx.paging.PagingData<com.d9tilov.android.transaction.domain.model.Transaction>> flow) {
        return null;
    }
}