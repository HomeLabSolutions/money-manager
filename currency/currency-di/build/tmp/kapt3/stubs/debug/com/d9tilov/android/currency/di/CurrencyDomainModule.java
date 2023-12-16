package com.d9tilov.android.currency.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0007\u00a8\u0006\f"}, d2 = {"Lcom/d9tilov/android/currency/di/CurrencyDomainModule;", "", "()V", "provideCurrencyInteractor", "Lcom/d9tilov/android/currency/domain/contract/CurrencyInteractor;", "currencyRepo", "Lcom/d9tilov/android/currency/domain/contract/CurrencyRepo;", "provideCurrencyObserver", "Lcom/d9tilov/android/currency/observer/contract/CurrencyUpdateObserver;", "currencyInteractor", "budgetInteractor", "Lcom/d9tilov/android/budget/domain/contract/BudgetInteractor;", "currency-di_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class CurrencyDomainModule {
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.currency.di.CurrencyDomainModule INSTANCE = null;
    
    private CurrencyDomainModule() {
        super();
    }
    
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.currency.domain.contract.CurrencyInteractor provideCurrencyInteractor(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.domain.contract.CurrencyRepo currencyRepo) {
        return null;
    }
    
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.currency.observer.contract.CurrencyUpdateObserver provideCurrencyObserver(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.domain.contract.CurrencyInteractor currencyInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.budget.domain.contract.BudgetInteractor budgetInteractor) {
        return null;
    }
}