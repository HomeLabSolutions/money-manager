package com.d9tilov.android.category.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0006\u0010\u0016\u001a\u00020\u0017J\u0006\u0010\u0018\u001a\u00020\u0017J\u000e\u0010\u0019\u001a\u00020\u00172\u0006\u0010\u001a\u001a\u00020\u001bR\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006\u001c"}, d2 = {"Lcom/d9tilov/android/category/ui/vm/CategoryCreationViewModel;", "Landroidx/lifecycle/ViewModel;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "categoryInteractor", "Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;", "billingInteractor", "Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;", "(Landroidx/lifecycle/SavedStateHandle;Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/d9tilov/android/category/ui/vm/CategoryCreationUiState;", "categoryArgs", "Lcom/d9tilov/android/category/ui/navigation/CategoryArgs$CategoryCreationArgs;", "categoryId", "", "transactionType", "Lcom/d9tilov/android/core/model/TransactionType;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "hideError", "", "save", "updateCategory", "category", "Lcom/d9tilov/android/category/domain/model/Category;", "category-ui_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class CategoryCreationViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.category.domain.contract.CategoryInteractor categoryInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.billing.domain.contract.BillingInteractor billingInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.category.ui.navigation.CategoryArgs.CategoryCreationArgs categoryArgs = null;
    private final long categoryId = 0L;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.core.model.TransactionType transactionType = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.d9tilov.android.category.ui.vm.CategoryCreationUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.category.ui.vm.CategoryCreationUiState> uiState = null;
    
    @javax.inject.Inject
    public CategoryCreationViewModel(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.SavedStateHandle savedStateHandle, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.contract.CategoryInteractor categoryInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.billing.domain.contract.BillingInteractor billingInteractor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.category.ui.vm.CategoryCreationUiState> getUiState() {
        return null;
    }
    
    public final void save() {
    }
    
    public final void updateCategory(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.model.Category category) {
    }
    
    public final void hideError() {
    }
}