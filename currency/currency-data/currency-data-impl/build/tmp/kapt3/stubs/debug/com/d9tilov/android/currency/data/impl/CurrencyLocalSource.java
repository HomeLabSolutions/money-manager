package com.d9tilov.android.currency.data.impl;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\'\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\fH\u0016J\u0019\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u0011H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012J\u000e\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\fH\u0016J\u0019\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0011H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012J\u001f\u0010\u0018\u001a\u00020\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u000e0\rH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001bJ\u0019\u0010\u001c\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\u000eH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001eJ\u0019\u0010\u001f\u001a\u00020\u00192\u0006\u0010\u0010\u001a\u00020\u0011H\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006 "}, d2 = {"Lcom/d9tilov/android/currency/data/impl/CurrencyLocalSource;", "Lcom/d9tilov/android/currency/data/contract/CurrencySource;", "dispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "preferencesStore", "Lcom/d9tilov/android/datastore/PreferencesStore;", "currencyListDao", "Lcom/d9tilov/android/database/dao/CurrencyListDao;", "mainCurrencyDao", "Lcom/d9tilov/android/database/dao/MainCurrencyDao;", "(Lkotlinx/coroutines/CoroutineDispatcher;Lcom/d9tilov/android/datastore/PreferencesStore;Lcom/d9tilov/android/database/dao/CurrencyListDao;Lcom/d9tilov/android/database/dao/MainCurrencyDao;)V", "getCurrencies", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/d9tilov/android/currency/domain/model/Currency;", "getCurrencyByCode", "code", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getMainCurrency", "Lcom/d9tilov/android/currency/domain/model/CurrencyMetaData;", "hasAlreadyUpdatedToday", "", "baseCurrency", "saveCurrencies", "", "currencies", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "currency", "(Lcom/d9tilov/android/currency/domain/model/Currency;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateMainCurrency", "currency-data-impl_debug"})
public final class CurrencyLocalSource implements com.d9tilov.android.currency.data.contract.CurrencySource {
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.CoroutineDispatcher dispatcher = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.datastore.PreferencesStore preferencesStore = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.database.dao.CurrencyListDao currencyListDao = null;
    @org.jetbrains.annotations.NotNull
    private final com.d9tilov.android.database.dao.MainCurrencyDao mainCurrencyDao = null;
    
    public CurrencyLocalSource(@com.d9tilov.android.network.dispatchers.Dispatcher(moneyManagerDispatcher = com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers.IO)
    @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineDispatcher dispatcher, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.datastore.PreferencesStore preferencesStore, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.dao.CurrencyListDao currencyListDao, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.dao.MainCurrencyDao mainCurrencyDao) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<com.d9tilov.android.currency.domain.model.CurrencyMetaData> getMainCurrency() {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object saveCurrencies(@org.jetbrains.annotations.NotNull
    java.util.List<com.d9tilov.android.currency.domain.model.Currency> currencies, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
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
    public java.lang.Object hasAlreadyUpdatedToday(@org.jetbrains.annotations.NotNull
    java.lang.String baseCurrency, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object update(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.domain.model.Currency currency, @org.jetbrains.annotations.NotNull
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
    @org.jetbrains.annotations.NotNull
    public kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.currency.domain.model.Currency>> getCurrencies() {
        return null;
    }
}