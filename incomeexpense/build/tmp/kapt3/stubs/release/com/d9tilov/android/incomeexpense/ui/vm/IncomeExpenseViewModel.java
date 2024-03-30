package com.d9tilov.android.incomeexpense.ui.vm;

import java.lang.System;

@dagger.hilt.android.lifecycle.HiltViewModel
@kotlin.Metadata(mv = {1, 7, 1}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0006\u0010\u0012\u001a\u00020\nJ\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\n0\u0014J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\nJ\u0006\u0010\u0018\u001a\u00020\u0016R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/d9tilov/android/incomeexpense/ui/vm/IncomeExpenseViewModel;", "Lcom/d9tilov/android/common_android/ui/base/BaseViewModel;", "Lcom/d9tilov/android/incomeexpense/navigation/IncomeExpenseNavigator;", "currencyInteractor", "Lcom/d9tilov/android/currency/domain/contract/CurrencyInteractor;", "billingInteractor", "Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;", "(Lcom/d9tilov/android/currency/domain/contract/CurrencyInteractor;Lcom/d9tilov/android/billing/domain/contract/BillingInteractor;)V", "currencyCodeStr", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "defaultCurrencyCode", "isPremium", "Lkotlinx/coroutines/flow/StateFlow;", "", "()Lkotlinx/coroutines/flow/StateFlow;", "updateCurrencyExceptionHandler", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "getCurrencyCode", "getCurrencyCodeAsync", "Lkotlinx/coroutines/flow/Flow;", "setCurrencyCode", "", "currencyCode", "setDefaultCurrencyCode", "incomeexpense_release"})
public final class IncomeExpenseViewModel extends com.d9tilov.android.common_android.ui.base.BaseViewModel<com.d9tilov.android.incomeexpense.navigation.IncomeExpenseNavigator> {
    private final com.d9tilov.android.currency.domain.contract.CurrencyInteractor currencyInteractor = null;
    private kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> currencyCodeStr;
    private final kotlinx.coroutines.CoroutineExceptionHandler updateCurrencyExceptionHandler = null;
    private java.lang.String defaultCurrencyCode = "USD";
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isPremium = null;
    
    @javax.inject.Inject
    public IncomeExpenseViewModel(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.domain.contract.CurrencyInteractor currencyInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.billing.domain.contract.BillingInteractor billingInteractor) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> isPremium() {
        return null;
    }
    
    public final void setCurrencyCode(@org.jetbrains.annotations.NotNull
    java.lang.String currencyCode) {
    }
    
    public final void setDefaultCurrencyCode() {
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.Flow<java.lang.String> getCurrencyCodeAsync() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getCurrencyCode() {
        return null;
    }
}