package com.d9tilov.android.currency.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u000e\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u000fR\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\u0016"}, d2 = {"Lcom/d9tilov/android/currency/ui/CurrencyViewModel;", "Landroidx/lifecycle/ViewModel;", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "currencyInteractor", "Lcom/d9tilov/android/currency/domain/contract/CurrencyInteractor;", "currencyUpdateObserver", "Lcom/d9tilov/android/currency/observer/contract/CurrencyUpdateObserver;", "(Landroidx/lifecycle/SavedStateHandle;Lcom/d9tilov/android/currency/domain/contract/CurrencyInteractor;Lcom/d9tilov/android/currency/observer/contract/CurrencyUpdateObserver;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/d9tilov/android/currency/ui/CurrencyUiState;", "currencyArgs", "Lcom/d9tilov/android/currency/ui/navigation/CurrencyArgs$CurrencyScreenArgs;", "selectedCurrency", "", "uiState", "getUiState", "()Lkotlinx/coroutines/flow/MutableStateFlow;", "changeCurrency", "Lkotlinx/coroutines/Job;", "code", "currency-ui_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class CurrencyViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.currency.observer.contract.CurrencyUpdateObserver currencyUpdateObserver = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.currency.ui.navigation.CurrencyArgs.CurrencyScreenArgs currencyArgs = null;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String selectedCurrency = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.d9tilov.android.currency.ui.CurrencyUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.d9tilov.android.currency.ui.CurrencyUiState> uiState = null;
    
    @javax.inject.Inject
    public CurrencyViewModel(@org.jetbrains.annotations.NotNull
    androidx.lifecycle.SavedStateHandle savedStateHandle, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.domain.contract.CurrencyInteractor currencyInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.observer.contract.CurrencyUpdateObserver currencyUpdateObserver) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.MutableStateFlow<com.d9tilov.android.currency.ui.CurrencyUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.Job changeCurrency(@org.jetbrains.annotations.NotNull
    java.lang.String code) {
        return null;
    }
}