package com.d9tilov.android.budget.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\u0011R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0012"}, d2 = {"Lcom/d9tilov/android/budget/ui/BudgetAmountViewModel;", "Landroidx/lifecycle/ViewModel;", "budgetInteractor", "Lcom/d9tilov/android/budget/domain/contract/BudgetInteractor;", "(Lcom/d9tilov/android/budget/domain/contract/BudgetInteractor;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/d9tilov/android/budget/ui/BudgetUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "changeBudgetAmount", "", "amount", "", "saveBudgetAmount", "Lkotlinx/coroutines/Job;", "budget-ui_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class BudgetAmountViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.budget.domain.contract.BudgetInteractor budgetInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.d9tilov.android.budget.ui.BudgetUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.budget.ui.BudgetUiState> uiState = null;
    
    @javax.inject.Inject
    public BudgetAmountViewModel(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.budget.domain.contract.BudgetInteractor budgetInteractor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.budget.ui.BudgetUiState> getUiState() {
        return null;
    }
    
    public final void changeBudgetAmount(@org.jetbrains.annotations.NotNull
    java.lang.String amount) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job saveBudgetAmount() {
        return null;
    }
}