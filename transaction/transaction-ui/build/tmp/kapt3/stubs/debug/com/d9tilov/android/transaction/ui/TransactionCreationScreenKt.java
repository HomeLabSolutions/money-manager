package com.d9tilov.android.transaction.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000H\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0002\u001a\b\u0010\u0000\u001a\u00020\u0001H\u0007\u001a\b\u0010\u0002\u001a\u00020\u0001H\u0007\u001aN\u0010\u0003\u001a\u00020\u00012\b\b\u0002\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\t2\u0018\u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\fH\u0007\u001a\u00aa\u0001\u0010\u000f\u001a\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u00112\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\t2\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00010\t2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00010\u00072\u0012\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\u00010\t2\u0018\u0010\u0018\u001a\u0014\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00010\f2\u0012\u0010\u0019\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u00010\t2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00010\u0007H\u0007\u00a8\u0006\u001c"}, d2 = {"DefaultTransactionCreationPreview", "", "ShowError", "TransactionCreationRoute", "viewModel", "Lcom/d9tilov/android/transaction/ui/vm/TransactionCreationViewModel;", "clickBack", "Lkotlin/Function0;", "clickCurrency", "Lkotlin/Function1;", "", "clickCategory", "Lkotlin/Function2;", "Lcom/d9tilov/android/core/model/TransactionType;", "Lcom/d9tilov/android/category/domain/model/CategoryDestination;", "TransactionCreationScreen", "uiState", "Lcom/d9tilov/android/transaction/ui/vm/TransactionUiState;", "onSumChanged", "onInStatisticsChanged", "", "onDescriptionChanged", "onBackClicked", "onCurrencyClicked", "onCategoryClicked", "onDateClicked", "", "onSaveClicked", "transaction-ui_debug"})
public final class TransactionCreationScreenKt {
    
    @androidx.compose.runtime.Composable
    public static final void TransactionCreationRoute(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.transaction.ui.vm.TransactionCreationViewModel viewModel, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> clickBack, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> clickCurrency, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.d9tilov.android.core.model.TransactionType, ? super com.d9tilov.android.category.domain.model.CategoryDestination, kotlin.Unit> clickCategory) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.material.ExperimentalMaterialApi.class})
    @androidx.compose.runtime.Composable
    public static final void TransactionCreationScreen(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.transaction.ui.vm.TransactionUiState uiState, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onSumChanged, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onInStatisticsChanged, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onDescriptionChanged, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onBackClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onCurrencyClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function2<? super com.d9tilov.android.core.model.TransactionType, ? super com.d9tilov.android.category.domain.model.CategoryDestination, kotlin.Unit> onCategoryClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onDateClicked, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onSaveClicked) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void ShowError() {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true)
    @androidx.compose.runtime.Composable
    public static final void DefaultTransactionCreationPreview() {
    }
}