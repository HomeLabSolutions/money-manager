package com.d9tilov.android.currency.ui.navigation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\b0\u0018\u00002\u00020\u0001:\u0001\u0003B\u0007\b\u0004\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcom/d9tilov/android/currency/ui/navigation/CurrencyArgs;", "", "()V", "CurrencyScreenArgs", "currency-ui_debug"})
public abstract class CurrencyArgs {
    
    private CurrencyArgs() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u000f\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0002\u0010\u0007R\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\n"}, d2 = {"Lcom/d9tilov/android/currency/ui/navigation/CurrencyArgs$CurrencyScreenArgs;", "", "savedStateHandle", "Landroidx/lifecycle/SavedStateHandle;", "(Landroidx/lifecycle/SavedStateHandle;)V", "currencyCode", "", "(Ljava/lang/String;)V", "getCurrencyCode", "()Ljava/lang/String;", "currency-ui_debug"})
    public static final class CurrencyScreenArgs {
        @org.jetbrains.annotations.Nullable
        private final java.lang.String currencyCode = null;
        
        public CurrencyScreenArgs(@org.jetbrains.annotations.Nullable
        java.lang.String currencyCode) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable
        public final java.lang.String getCurrencyCode() {
            return null;
        }
        
        public CurrencyScreenArgs(@org.jetbrains.annotations.NotNull
        androidx.lifecycle.SavedStateHandle savedStateHandle) {
            super();
        }
    }
}