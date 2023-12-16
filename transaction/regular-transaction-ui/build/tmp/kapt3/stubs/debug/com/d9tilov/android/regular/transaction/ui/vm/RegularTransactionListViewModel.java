package com.d9tilov.android.regular.transaction.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u0016"}, d2 = {"Lcom/d9tilov/android/regular/transaction/ui/vm/RegularTransactionListViewModel;", "Landroidx/lifecycle/ViewModel;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "regularTransactionInteractor", "Lcom/d9tilov/android/regular/transaction/domain/contract/RegularTransactionInteractor;", "(Landroidx/lifecycle/SavedStateHandle;Lcom/d9tilov/android/regular/transaction/domain/contract/RegularTransactionInteractor;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/d9tilov/android/regular/transaction/ui/vm/RegularTransactionListState;", "regularTransactionArgs", "Lcom/d9tilov/android/regular/transaction/ui/navigator/RegularTransactionArgs$RegularTransactionListArgs;", "transactionType", "Lcom/d9tilov/android/core/model/TransactionType;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "removeTransaction", "Lkotlinx/coroutines/Job;", "transaction", "Lcom/d9tilov/android/regular/transaction/domain/model/RegularTransaction;", "regular-transaction-ui_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class RegularTransactionListViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor regularTransactionInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.regular.transaction.ui.navigator.RegularTransactionArgs.RegularTransactionListArgs regularTransactionArgs = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.core.model.TransactionType transactionType = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionListState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionListState> uiState = null;
    
    @javax.inject.Inject
    public RegularTransactionListViewModel(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.SavedStateHandle savedStateHandle, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor regularTransactionInteractor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionListState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job removeTransaction(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.model.RegularTransaction transaction) {
        return null;
    }
}