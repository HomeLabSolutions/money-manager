package com.d9tilov.android.regular.transaction.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007\u00a8\u0006\u000b"}, d2 = {"Lcom/d9tilov/android/regular/transaction/di/RegularTransactionDomainModule;", "", "()V", "provideRegularTransactionInteractor", "Lcom/d9tilov/android/regular/transaction/domain/contract/RegularTransactionInteractor;", "regularTransactionRepo", "Lcom/d9tilov/android/regular/transaction/domain/contract/RegularTransactionRepo;", "currencyInteractor", "Lcom/d9tilov/android/currency/domain/contract/CurrencyInteractor;", "categoryInteractor", "Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;", "regular-transaction-di_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.android.components.ActivityRetainedComponent.class})
public final class RegularTransactionDomainModule {
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.regular.transaction.di.RegularTransactionDomainModule INSTANCE = null;
    
    private RegularTransactionDomainModule() {
        super();
    }
    
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor provideRegularTransactionInteractor(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionRepo regularTransactionRepo, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.domain.contract.CurrencyInteractor currencyInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.contract.CategoryInteractor categoryInteractor) {
        return null;
    }
}