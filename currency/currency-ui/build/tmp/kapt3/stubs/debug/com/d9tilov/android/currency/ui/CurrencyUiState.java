package com.d9tilov.android.currency.ui;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bv\u0018\u00002\u00020\u0001:\u0002\n\u000bR\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0012\u0010\u0007\u001a\u00020\bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\t\u0082\u0001\u0002\f\r\u00a8\u0006\u000e"}, d2 = {"Lcom/d9tilov/android/currency/ui/CurrencyUiState;", "", "errorMessages", "", "Lcom/d9tilov/android/core/model/ErrorMessage;", "getErrorMessages", "()Ljava/util/List;", "isLoading", "", "()Z", "HasCurrencies", "NoCurrencies", "Lcom/d9tilov/android/currency/ui/CurrencyUiState$HasCurrencies;", "Lcom/d9tilov/android/currency/ui/CurrencyUiState$NoCurrencies;", "currency-ui_debug"})
public abstract interface CurrencyUiState {
    
    public abstract boolean isLoading();
    
    @org.jetbrains.annotations.NotNull
    public abstract java.util.List<com.d9tilov.android.core.model.ErrorMessage> getErrorMessages();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B-\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003\u00a2\u0006\u0002\u0010\tJ\u000f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0006H\u00c6\u0003J\u000f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H\u00c6\u0003J3\u0010\u0011\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\u000e\b\u0002\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003H\u00c6\u0001J\u0013\u0010\u0012\u001a\u00020\u00062\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u00d6\u0003J\t\u0010\u0015\u001a\u00020\u0016H\u00d6\u0001J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\b0\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0014\u0010\u0005\u001a\u00020\u0006X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\r\u00a8\u0006\u0019"}, d2 = {"Lcom/d9tilov/android/currency/ui/CurrencyUiState$HasCurrencies;", "Lcom/d9tilov/android/currency/ui/CurrencyUiState;", "currencyList", "", "Lcom/d9tilov/android/currency/domain/model/DomainCurrency;", "isLoading", "", "errorMessages", "Lcom/d9tilov/android/core/model/ErrorMessage;", "(Ljava/util/List;ZLjava/util/List;)V", "getCurrencyList", "()Ljava/util/List;", "getErrorMessages", "()Z", "component1", "component2", "component3", "copy", "equals", "other", "", "hashCode", "", "toString", "", "currency-ui_debug"})
    public static final class HasCurrencies implements com.d9tilov.android.currency.ui.CurrencyUiState {
        @org.jetbrains.annotations.NotNull
        private final java.util.List<com.d9tilov.android.currency.domain.model.DomainCurrency> currencyList = null;
        private final boolean isLoading = false;
        @org.jetbrains.annotations.NotNull
        private final java.util.List<com.d9tilov.android.core.model.ErrorMessage> errorMessages = null;
        
        public HasCurrencies(@org.jetbrains.annotations.NotNull
        java.util.List<com.d9tilov.android.currency.domain.model.DomainCurrency> currencyList, boolean isLoading, @org.jetbrains.annotations.NotNull
        java.util.List<com.d9tilov.android.core.model.ErrorMessage> errorMessages) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.util.List<com.d9tilov.android.currency.domain.model.DomainCurrency> getCurrencyList() {
            return null;
        }
        
        @java.lang.Override
        public boolean isLoading() {
            return false;
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public java.util.List<com.d9tilov.android.core.model.ErrorMessage> getErrorMessages() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.util.List<com.d9tilov.android.currency.domain.model.DomainCurrency> component1() {
            return null;
        }
        
        public final boolean component2() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.util.List<com.d9tilov.android.core.model.ErrorMessage> component3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.currency.ui.CurrencyUiState.HasCurrencies copy(@org.jetbrains.annotations.NotNull
        java.util.List<com.d9tilov.android.currency.domain.model.DomainCurrency> currencyList, boolean isLoading, @org.jetbrains.annotations.NotNull
        java.util.List<com.d9tilov.android.core.model.ErrorMessage> errorMessages) {
            return null;
        }
        
        @java.lang.Override
        public boolean equals(@org.jetbrains.annotations.Nullable
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u001f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u00a2\u0006\u0002\u0010\u0007J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0003J#\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\u000e\b\u0002\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u00032\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u001a\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lcom/d9tilov/android/currency/ui/CurrencyUiState$NoCurrencies;", "Lcom/d9tilov/android/currency/ui/CurrencyUiState;", "isLoading", "", "errorMessages", "", "Lcom/d9tilov/android/core/model/ErrorMessage;", "(ZLjava/util/List;)V", "getErrorMessages", "()Ljava/util/List;", "()Z", "component1", "component2", "copy", "equals", "other", "", "hashCode", "", "toString", "", "currency-ui_debug"})
    public static final class NoCurrencies implements com.d9tilov.android.currency.ui.CurrencyUiState {
        private final boolean isLoading = false;
        @org.jetbrains.annotations.NotNull
        private final java.util.List<com.d9tilov.android.core.model.ErrorMessage> errorMessages = null;
        
        public NoCurrencies(boolean isLoading, @org.jetbrains.annotations.NotNull
        java.util.List<com.d9tilov.android.core.model.ErrorMessage> errorMessages) {
            super();
        }
        
        @java.lang.Override
        public boolean isLoading() {
            return false;
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public java.util.List<com.d9tilov.android.core.model.ErrorMessage> getErrorMessages() {
            return null;
        }
        
        public NoCurrencies() {
            super();
        }
        
        public final boolean component1() {
            return false;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.util.List<com.d9tilov.android.core.model.ErrorMessage> component2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.d9tilov.android.currency.ui.CurrencyUiState.NoCurrencies copy(boolean isLoading, @org.jetbrains.annotations.NotNull
        java.util.List<com.d9tilov.android.core.model.ErrorMessage> errorMessages) {
            return null;
        }
        
        @java.lang.Override
        public boolean equals(@org.jetbrains.annotations.Nullable
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override
        @org.jetbrains.annotations.NotNull
        public java.lang.String toString() {
            return null;
        }
    }
}