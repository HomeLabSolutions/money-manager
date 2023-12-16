package com.d9tilov.android.transaction.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J8\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0007\u00a8\u0006\u0011"}, d2 = {"Lcom/d9tilov/android/transaction/di/TransactionDomainModule;", "", "()V", "provideTransactionInteractor", "Lcom/d9tilov/android/transaction/domain/contract/TransactionInteractor;", "transactionRepo", "Lcom/d9tilov/android/transaction/domain/contract/TransactionRepo;", "regularTransactionInteractor", "Lcom/d9tilov/android/regular/transaction/domain/contract/RegularTransactionInteractor;", "categoryInteractor", "Lcom/d9tilov/android/category/domain/contract/CategoryInteractor;", "userInteractor", "Lcom/d9tilov/android/user/domain/contract/UserInteractor;", "currencyInteractor", "Lcom/d9tilov/android/currency/domain/contract/CurrencyInteractor;", "budgetInteractor", "Lcom/d9tilov/android/budget/domain/contract/BudgetInteractor;", "transaction-di_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.android.components.ActivityRetainedComponent.class})
public final class TransactionDomainModule {
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.transaction.di.TransactionDomainModule INSTANCE = null;
    
    private TransactionDomainModule() {
        super();
    }
    
    @dagger.Provides
    @dagger.hilt.android.scopes.ActivityRetainedScoped
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.transaction.domain.contract.TransactionInteractor provideTransactionInteractor(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.transaction.domain.contract.TransactionRepo transactionRepo, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionInteractor regularTransactionInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.category.domain.contract.CategoryInteractor categoryInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.user.domain.contract.UserInteractor userInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.domain.contract.CurrencyInteractor currencyInteractor, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.budget.domain.contract.BudgetInteractor budgetInteractor) {
        return null;
    }
}