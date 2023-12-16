package com.d9tilov.android.regular.transaction.ui.navigator;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000F\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a&\u0010\u0004\u001a\u00020\u0005*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f\u001a\u001e\u0010\r\u001a\u00020\u0005*\u00020\u00062\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0007\u001a\u00020\b\u001aT\u0010\u000e\u001a\u00020\u0005*\u00020\u000f2\u0018\u0010\u0010\u001a\u0014\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00050\u00112\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00050\u00142\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00050\u00162\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00050\u0016\u001a2\u0010\u0018\u001a\u00020\u0005*\u00020\u000f2\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00050\u00162\u0018\u0010\u0019\u001a\u0014\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00050\u0011\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0086T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0086T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"regularTransactionCreationNavigationRoute", "", "regularTransactionIdArgs", "regularTransactionListNavigationRoute", "navigateToRegularTransactionCreationScreen", "", "Landroidx/navigation/NavController;", "transactionType", "Lcom/d9tilov/android/core/model/TransactionType;", "transactionId", "", "navOptions", "Landroidx/navigation/NavOptions;", "navigateToRegularTransactionListScreen", "regularTransactionCreationScreen", "Landroidx/navigation/NavGraphBuilder;", "onCategoryClick", "Lkotlin/Function2;", "Lcom/d9tilov/android/category/domain/model/CategoryDestination;", "onCurrencyClick", "Lkotlin/Function1;", "onSaveClick", "Lkotlin/Function0;", "clickBack", "regularTransactionListScreen", "openCreationTransaction", "regular-transaction-ui_debug"})
public final class RegularIncomeExpenseNavigatorKt {
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String regularTransactionIdArgs = "regular_transaction_id";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String regularTransactionListNavigationRoute = "regular_transaction_list_route";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String regularTransactionCreationNavigationRoute = "regular_transaction_creation_route";
    
    public static final void navigateToRegularTransactionListScreen(@org.jetbrains.annotations.NotNull
    androidx.navigation.NavController $this$navigateToRegularTransactionListScreen, @org.jetbrains.annotations.Nullable
    androidx.navigation.NavOptions navOptions, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.model.TransactionType transactionType) {
    }
    
    public static final void regularTransactionListScreen(@org.jetbrains.annotations.NotNull
    androidx.navigation.NavGraphBuilder $this$regularTransactionListScreen, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> clickBack, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.d9tilov.android.core.model.TransactionType, ? super java.lang.Long, kotlin.Unit> openCreationTransaction) {
    }
    
    public static final void navigateToRegularTransactionCreationScreen(@org.jetbrains.annotations.NotNull
    androidx.navigation.NavController $this$navigateToRegularTransactionCreationScreen, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.model.TransactionType transactionType, long transactionId, @org.jetbrains.annotations.Nullable
    androidx.navigation.NavOptions navOptions) {
    }
    
    public static final void regularTransactionCreationScreen(@org.jetbrains.annotations.NotNull
    androidx.navigation.NavGraphBuilder $this$regularTransactionCreationScreen, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.d9tilov.android.core.model.TransactionType, ? super com.d9tilov.android.category.domain.model.CategoryDestination, kotlin.Unit> onCategoryClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onCurrencyClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onSaveClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> clickBack) {
    }
}