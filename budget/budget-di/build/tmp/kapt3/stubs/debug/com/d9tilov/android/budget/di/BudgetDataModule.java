package com.d9tilov.android.budget.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\"\u0010\u0003\u001a\u00020\u00042\b\b\u0001\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u0004H\u0007\u00a8\u0006\u000e"}, d2 = {"Lcom/d9tilov/android/budget/di/BudgetDataModule;", "", "()V", "provideBudgetLocalSource", "Lcom/d9tilov/android/budget/data/contract/BudgetSource;", "dispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "preferencesStore", "Lcom/d9tilov/android/datastore/PreferencesStore;", "database", "Lcom/d9tilov/android/database/AppDatabase;", "provideBudgetRepo", "Lcom/d9tilov/android/budget/domain/contract/BudgetRepo;", "budgetSource", "budget-di_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class BudgetDataModule {
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.budget.di.BudgetDataModule INSTANCE = null;
    
    private BudgetDataModule() {
        super();
    }
    
    @dagger.Provides
    @javax.inject.Singleton
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.budget.data.contract.BudgetSource provideBudgetLocalSource(@com.d9tilov.android.network.dispatchers.Dispatcher(moneyManagerDispatcher = com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers.IO)
    @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineDispatcher dispatcher, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.datastore.PreferencesStore preferencesStore, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.AppDatabase database) {
        return null;
    }
    
    @dagger.Provides
    @javax.inject.Singleton
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.budget.domain.contract.BudgetRepo provideBudgetRepo(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.budget.data.contract.BudgetSource budgetSource) {
        return null;
    }
}