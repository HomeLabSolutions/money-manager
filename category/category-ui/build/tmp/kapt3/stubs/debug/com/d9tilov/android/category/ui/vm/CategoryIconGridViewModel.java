package com.d9tilov.android.category.ui.vm;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\t0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R \u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\u00150\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u000b0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0011R\u0014\u0010\u0018\u001a\u00020\u0019X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001d0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0011\u00a8\u0006\u001f"}, d2 = {"Lcom/d9tilov/android/category/ui/vm/CategoryIconGridViewModel;", "Landroidx/lifecycle/ViewModel;", "billingInteractor", "Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "(Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;Landroidx/lifecycle/SavedStateHandle;)V", "_categoryIconId", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_isPremium", "", "categoryArgs", "Lcom/d9tilov/android/category/ui/navigation/CategoryArgs$CategoryIconsArgs;", "categoryIconId", "Lkotlinx/coroutines/flow/StateFlow;", "getCategoryIconId", "()Lkotlinx/coroutines/flow/StateFlow;", "groupedPaidIcons", "", "Lcom/d9tilov/android/category/domain/model/CategoryGroup;", "", "iconGroup", "isPremium", "route", "", "getRoute", "()Ljava/lang/String;", "uiState", "Lcom/d9tilov/android/category/ui/vm/CategoryIconGridUiState;", "getUiState", "category-ui_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class CategoryIconGridViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.billing.domain.contract.BillingInteractor billingInteractor = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.Map<com.d9tilov.android.category.domain.model.CategoryGroup, java.util.List<java.lang.Integer>> groupedPaidIcons = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.category.ui.navigation.CategoryArgs.CategoryIconsArgs categoryArgs = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.category.domain.model.CategoryGroup iconGroup = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String route = "";
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> _categoryIconId = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _isPremium = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> categoryIconId = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isPremium = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.category.ui.vm.CategoryIconGridUiState> uiState = null;
    
    @javax.inject.Inject
    public CategoryIconGridViewModel(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.billing.domain.contract.BillingInteractor billingInteractor, @org.jetbrains.annotations.NotNull
    androidx.lifecycle.SavedStateHandle savedStateHandle) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getRoute() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Integer> getCategoryIconId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isPremium() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.d9tilov.android.category.ui.vm.CategoryIconGridUiState> getUiState() {
        return null;
    }
}