package com.d9tilov.android.currency.data.impl;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0014\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nH\u0016J\u0019\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u000fH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010J\u000e\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\nH\u0016J\u0019\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u000fH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010J\u0011\u0010\u0016\u001a\u00020\u0017H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0018J\u0019\u0010\u0019\u001a\u00020\u00172\u0006\u0010\u000e\u001a\u00020\u000fH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001a"}, d2 = {"Lcom/d9tilov/android/currency/data/impl/CurrencyDataRepo;", "Lcom/d9tilov/android/currency/domain/contract/CurrencyRepo;", "currencySource", "Lcom/d9tilov/android/currency/data/contract/CurrencySource;", "currencyApi", "Lcom/d9tilov/android/network/CurrencyApi;", "preferencesStore", "Lcom/d9tilov/android/datastore/PreferencesStore;", "(Lcom/d9tilov/android/currency/data/contract/CurrencySource;Lcom/d9tilov/android/network/CurrencyApi;Lcom/d9tilov/android/datastore/PreferencesStore;)V", "getCurrencies", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/d9tilov/android/currency/domain/model/Currency;", "getCurrencyByCode", "code", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMainCurrency", "Lcom/d9tilov/android/currency/domain/model/CurrencyMetaData;", "hasAlreadyUpdatedToday", "", "baseCurrency", "updateCurrencies", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateMainCurrency", "currency-data-impl_debug"})
public final class CurrencyDataRepo implements com.d9tilov.android.currency.domain.contract.CurrencyRepo {
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.currency.data.contract.CurrencySource currencySource = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.network.CurrencyApi currencyApi = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.datastore.PreferencesStore preferencesStore = null;
    
    public CurrencyDataRepo(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.data.contract.CurrencySource currencySource, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.network.CurrencyApi currencyApi, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.datastore.PreferencesStore preferencesStore) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.currency.domain.model.Currency>> getCurrencies() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<com.d9tilov.android.currency.domain.model.CurrencyMetaData> getMainCurrency() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object getCurrencyByCode(@org.jetbrains.annotations.NotNull
    java.lang.String code, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.d9tilov.android.currency.domain.model.Currency> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object updateCurrencies(@org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object updateMainCurrency(@org.jetbrains.annotations.NotNull
    java.lang.String code, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object hasAlreadyUpdatedToday(@org.jetbrains.annotations.NotNull
    java.lang.String baseCurrency, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
}