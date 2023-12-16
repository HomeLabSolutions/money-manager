package com.d9tilov.android.regular.transaction.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bJ\u000e\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u000fJ\u000e\u0010\u001e\u001a\u00020\u00192\u0006\u0010\u001f\u001a\u00020 J\u000e\u0010!\u001a\u00020\u00192\u0006\u0010\"\u001a\u00020\u001bJ\u000e\u0010#\u001a\u00020\u00192\u0006\u0010$\u001a\u00020%J\u000e\u0010&\u001a\u00020\u00192\u0006\u0010\'\u001a\u00020\u001bJ\u000e\u0010(\u001a\u00020\u00192\u0006\u0010$\u001a\u00020)R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006*"}, d2 = {"Lcom/d9tilov/android/regular/transaction/ui/vm/RegularTransactionCreationViewModel;", "Landroidx/lifecycle/ViewModel;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "regularTransactionInteractor", "Lcom/d9tilov/android/regular/transaction/domain/contract/RegularTransactionInteractor;", "categoryInteractor", "Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;", "(Landroidx/lifecycle/SavedStateHandle;Lcom/d9tilov/android/regular/transaction/domain/contract/RegularTransactionInteractor;Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/d9tilov/android/regular/transaction/ui/vm/RegularTransactionCreationUiState;", "regTransactionArgs", "Lcom/d9tilov/android/regular/transaction/ui/navigator/RegularTransactionArgs$RegularTransactionCreationArgs;", "transactionId", "", "transactionType", "Lcom/d9tilov/android/core/model/TransactionType;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "saveOrUpdate", "Lkotlinx/coroutines/Job;", "updateAmount", "", "amount", "", "updateCategory", "id", "updateCurPeriodItem", "menuItem", "Lcom/d9tilov/android/regular/transaction/ui/vm/PeriodMenuItem;", "updateCurrencyCode", "code", "updateDayOfMonth", "day", "", "updateDescription", "description", "updateWeekDay", "Lcom/d9tilov/android/regular/transaction/ui/vm/DaysInWeek;", "regular-transaction-ui_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class RegularTransactionCreationViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor regularTransactionInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.category.domain.contract.CategoryInteractor categoryInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.regular.transaction.ui.navigator.RegularTransactionArgs.RegularTransactionCreationArgs regTransactionArgs = null;
    private final long transactionId = 0L;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.core.model.TransactionType transactionType = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionCreationUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionCreationUiState> uiState = null;
    
    @javax.inject.Inject
    public RegularTransactionCreationViewModel(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.SavedStateHandle savedStateHandle, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor regularTransactionInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.contract.CategoryInteractor categoryInteractor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionCreationUiState> getUiState() {
        return null;
    }
    
    public final void updateAmount(@org.jetbrains.annotations.NotNull
    java.lang.String amount) {
    }
    
    public final void updateCurrencyCode(@org.jetbrains.annotations.NotNull
    java.lang.String code) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job updateCategory(long id) {
        return null;
    }
    
    public final void updateCurPeriodItem(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.ui.vm.PeriodMenuItem menuItem) {
    }
    
    public final void updateWeekDay(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.ui.vm.DaysInWeek day) {
    }
    
    public final void updateDayOfMonth(int day) {
    }
    
    public final void updateDescription(@org.jetbrains.annotations.NotNull
    java.lang.String description) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job saveOrUpdate() {
        return null;
    }
}