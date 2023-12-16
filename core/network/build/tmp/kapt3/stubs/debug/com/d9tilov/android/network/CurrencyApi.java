package com.d9tilov.android.network;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J%\u0010\u0002\u001a\u00020\u00032\b\b\u0003\u0010\u0004\u001a\u00020\u00052\b\b\u0003\u0010\u0006\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\b"}, d2 = {"Lcom/d9tilov/android/network/CurrencyApi;", "", "getCurrencies", "Lcom/d9tilov/android/network/model/CurrencyResponse;", "baseCurrency", "", "key", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "network_debug"})
public abstract interface CurrencyApi {
    
    @retrofit2.http.GET(value = "{api_key}/latest/{base}")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getCurrencies(@retrofit2.http.Path(value = "base")
    @org.jetbrains.annotations.NotNull
    java.lang.String baseCurrency, @retrofit2.http.Path(value = "api_key")
    @org.jetbrains.annotations.NotNull
    java.lang.String key, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.d9tilov.android.network.model.CurrencyResponse> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}