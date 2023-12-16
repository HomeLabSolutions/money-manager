package com.d9tilov.android.category.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u000e\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eR\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0010\u001a\u00020\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\u0014\u001a\u00020\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001a\u00a8\u0006\u001f"}, d2 = {"Lcom/d9tilov/android/category/ui/vm/CategoryListViewModel;", "Landroidx/lifecycle/ViewModel;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "categoryInteractor", "Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;", "transactionInteractor", "Lcom/d9tilov/android/transaction/domain/contract/TransactionInteractor;", "regularTransactionInteractor", "Lcom/d9tilov/android/regular/transaction/domain/contract/RegularTransactionInteractor;", "(Landroidx/lifecycle/SavedStateHandle;Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;Lcom/d9tilov/android/transaction/domain/contract/TransactionInteractor;Lcom/d9tilov/android/regular/transaction/domain/contract/RegularTransactionInteractor;)V", "_uiState", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/d9tilov/android/category/ui/vm/CategoryUiState;", "categoryArgs", "Lcom/d9tilov/android/category/ui/navigation/CategoryArgs$CategoryListArgs;", "destination", "Lcom/d9tilov/android/category/domain/model/CategoryDestination;", "getDestination", "()Lcom/d9tilov/android/category/domain/model/CategoryDestination;", "transactionType", "Lcom/d9tilov/android/core/model/TransactionType;", "getTransactionType", "()Lcom/d9tilov/android/core/model/TransactionType;", "uiState", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "remove", "", "category", "Lcom/d9tilov/android/category/domain/model/Category;", "category-ui_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class CategoryListViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.category.domain.contract.CategoryInteractor categoryInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.transaction.domain.contract.TransactionInteractor transactionInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor regularTransactionInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.category.ui.navigation.CategoryArgs.CategoryListArgs categoryArgs = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.core.model.TransactionType transactionType = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.category.domain.model.CategoryDestination destination = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.category.ui.vm.CategoryUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.category.ui.vm.CategoryUiState> uiState = null;
    
    @javax.inject.Inject
    public CategoryListViewModel(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.SavedStateHandle savedStateHandle, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.contract.CategoryInteractor categoryInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.transaction.domain.contract.TransactionInteractor transactionInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor regularTransactionInteractor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.core.model.TransactionType getTransactionType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.category.domain.model.CategoryDestination getDestination() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.category.ui.vm.CategoryUiState> getUiState() {
        return null;
    }
    
    public final void remove(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.Category category) {
    }
}