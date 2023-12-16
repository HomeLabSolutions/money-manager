package com.d9tilov.android.currency.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\u001a3\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032!\u0010\u0004\u001a\u001d\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u0002\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001aC\u0010\b\u001a\u00020\u00012\b\b\u0002\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00010\f2!\u0010\r\u001a\u001d\u0012\u0013\u0012\u00110\u000e\u00a2\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u000f\u0012\u0004\u0012\u00020\u00010\u0005H\u0007\u001aW\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0011\u001a\u00020\u00122\b\b\u0002\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162#\b\u0002\u0010\r\u001a\u001d\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u0002\u0012\u0004\u0012\u00020\u00010\u00052\u000e\b\u0002\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00010\fH\u0007\u001a\b\u0010\u0018\u001a\u00020\u0001H\u0007\u00a8\u0006\u0019"}, d2 = {"CurrencyItem", "", "currency", "Lcom/d9tilov/android/currency/domain/model/DomainCurrency;", "clickCallback", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "CurrencyListRoute", "viewModel", "Lcom/d9tilov/android/currency/ui/CurrencyViewModel;", "clickBack", "Lkotlin/Function0;", "onChooseCurrency", "", "currencyCode", "CurrencyListScreen", "currencyUiState", "Lcom/d9tilov/android/currency/ui/CurrencyUiState;", "modifier", "Landroidx/compose/ui/Modifier;", "showToolbar", "", "onClickBack", "DefaultPreviewCurrencyItem", "currency-ui_debug"})
public final class CurrencyListScreenKt {
    
    @androidx.compose.runtime.Composable
    public static final void CurrencyListRoute(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.ui.CurrencyViewModel viewModel, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> clickBack, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onChooseCurrency) {
    }
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.foundation.layout.ExperimentalLayoutApi.class})
    @androidx.compose.runtime.Composable
    public static final void CurrencyListScreen(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.ui.CurrencyUiState currencyUiState, @org.jetbrains.annotations.NotNull
    androidx.compose.ui.Modifier modifier, boolean showToolbar, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.d9tilov.android.currency.domain.model.DomainCurrency, kotlin.Unit> onChooseCurrency, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<kotlin.Unit> onClickBack) {
    }
    
    @androidx.compose.runtime.Composable
    public static final void CurrencyItem(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.domain.model.DomainCurrency currency, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.d9tilov.android.currency.domain.model.DomainCurrency, kotlin.Unit> clickCallback) {
    }
    
    @androidx.compose.ui.tooling.preview.Preview(showBackground = true)
    @androidx.compose.runtime.Composable
    public static final void DefaultPreviewCurrencyItem() {
    }
}