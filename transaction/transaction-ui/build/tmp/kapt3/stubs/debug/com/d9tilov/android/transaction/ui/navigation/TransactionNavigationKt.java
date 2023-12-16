package com.d9tilov.android.transaction.ui.navigation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000@\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u001e\u0010\u0003\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u001aF\u0010\n\u001a\u00020\u0004*\u00020\u000b2\f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\r2\u0018\u0010\u000e\u001a\u0014\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00040\u000f2\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00040\u0013\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0086T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"transactionIdArg", "", "transactionNavigationRoute", "navigateToTransactionScreen", "", "Landroidx/navigation/NavController;", "transactionId", "", "navOptions", "Landroidx/navigation/NavOptions;", "transactionCreationScreen", "Landroidx/navigation/NavGraphBuilder;", "clickBack", "Lkotlin/Function0;", "onCategoryClick", "Lkotlin/Function2;", "Lcom/d9tilov/android/core/model/TransactionType;", "Lcom/d9tilov/android/category/domain/model/CategoryDestination;", "onCurrencyClick", "Lkotlin/Function1;", "transaction-ui_debug"})
public final class TransactionNavigationKt {
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String transactionNavigationRoute = "transaction_screen";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String transactionIdArg = "transaction_id";
    
    public static final void navigateToTransactionScreen(@org.jetbrains.annotations.NotNull
    androidx.navigation.NavController $this$navigateToTransactionScreen, long transactionId, @org.jetbrains.annotations.Nullable
    androidx.navigation.NavOptions navOptions) {
    }
    
    public static final void transactionCreationScreen(@org.jetbrains.annotations.NotNull
    androidx.navigation.NavGraphBuilder $this$transactionCreationScreen, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> clickBack, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.d9tilov.android.core.model.TransactionType, ? super com.d9tilov.android.category.domain.model.CategoryDestination, kotlin.Unit> onCategoryClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onCurrencyClick) {
    }
}