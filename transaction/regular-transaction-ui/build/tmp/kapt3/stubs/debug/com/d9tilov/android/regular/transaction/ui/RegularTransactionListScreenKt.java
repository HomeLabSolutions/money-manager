package com.d9tilov.android.regular.transaction.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000Z\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\b\u0010\u0000\u001a\u00020\u0001H\u0007\u001a3\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00042!\u0010\u0005\u001a\u001d\u0012\u0013\u0012\u00110\u0004\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0004\u0012\u00020\u00010\u0006H\u0007\u001aN\u0010\n\u001a\u00020\u00012\b\b\u0002\u0010\u000b\u001a\u00020\f2\u0018\u0010\r\u001a\u0014\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00010\u000e2\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00010\u0012H\u0007\u001ae\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u0014\u001a\u00020\u00152#\b\u0002\u0010\u0016\u001a\u001d\u0012\u0013\u0012\u00110\u0004\u00a2\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\t\u0012\u0004\u0012\u00020\u00010\u00062\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00010\u00062\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00010\u00122\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00010\u0012H\u0007\u001a\u0018\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0002\u001a\u0018\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u00102\u0006\u0010\b\u001a\u00020\u001aH\u0002\u00a8\u0006\""}, d2 = {"DefaultRegularTransactionListPreview", "", "RegularTransactionItem", "transaction", "Lcom/d9tilov/android/regular/transaction/domain/model/RegularTransaction;", "onItemClicked", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "currency", "RegularTransactionListRoute", "viewModel", "Lcom/d9tilov/android/regular/transaction/ui/vm/RegularTransactionListViewModel;", "onAddClicked", "Lkotlin/Function2;", "Lcom/d9tilov/android/core/model/TransactionType;", "", "clickBack", "Lkotlin/Function0;", "RegularTransactionListScreen", "uiState", "Lcom/d9tilov/android/regular/transaction/ui/vm/RegularTransactionListState;", "onTransactionClicked", "onDeleteTransactionConfirmClicked", "onBackClicked", "getWeekDayString", "", "context", "Landroid/content/Context;", "day", "", "mockCategory", "Lcom/d9tilov/android/category/domain/model/Category;", "id", "regular-transaction-ui_debug"})
public final class RegularTransactionListScreenKt {
    
    @androidx.compose.runtime.Composable
    public static final void RegularTransactionListRoute(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionListViewModel viewModel, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.d9tilov.android.core.model.TransactionType, ? super java.lang.Long, kotlin.Unit> onAddClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.d9tilov.android.regular.transaction.domain.model.RegularTransaction, kotlin.Unit> onItemClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> clickBack) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable
    public static final void RegularTransactionListScreen(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.ui.vm.RegularTransactionListState uiState, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.d9tilov.android.regular.transaction.domain.model.RegularTransaction, kotlin.Unit> onTransactionClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.d9tilov.android.regular.transaction.domain.model.RegularTransaction, kotlin.Unit> onDeleteTransactionConfirmClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onAddClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClicked) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void RegularTransactionItem(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.model.RegularTransaction transaction, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.d9tilov.android.regular.transaction.domain.model.RegularTransaction, kotlin.Unit> onItemClicked) {
    }
    
    private static final java.lang.String getWeekDayString(android.content.Context context, int day) {
        return null;
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true)
    @androidx.compose.runtime.Composable
    public static final void DefaultRegularTransactionListPreview() {
    }
    
    private static final com.d9tilov.android.category.domain.model.Category mockCategory(long id, java.lang.String name) {
        return null;
    }
}