package com.d9tilov.android.network.dispatchers;

@javax.inject.Qualifier
@kotlin.annotation.Retention(value = kotlin.annotation.AnnotationRetention.RUNTIME)
@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\u0002\u0018\u00002\u00020\u0001B\b\u0012\u0006\u0010\u0002\u001a\u00020\u0003R\u000f\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0006\u001a\u0004\b\u0002\u0010\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/d9tilov/android/network/dispatchers/Dispatcher;", "", "moneyManagerDispatcher", "Lcom/d9tilov/android/network/dispatchers/MoneyManagerDispatchers;", "()Lcom/d9tilov/android/network/dispatchers/MoneyManagerDispatchers;", "network_debug"})
public abstract @interface Dispatcher {
    
    public abstract com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers moneyManagerDispatcher();
}