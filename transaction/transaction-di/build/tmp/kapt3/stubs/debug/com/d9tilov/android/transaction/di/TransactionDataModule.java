package com.d9tilov.android.transaction.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\"\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\bH\u0007\u00a8\u0006\u0010"}, d2 = {"Lcom/d9tilov/android/transaction/di/TransactionDataModule;", "", "()V", "provideTransactionDao", "Lcom/d9tilov/android/database/dao/TransactionDao;", "appDatabase", "Lcom/d9tilov/android/database/AppDatabase;", "provideTransactionLocalSource", "Lcom/d9tilov/android/transaction/data/contract/TransactionSource;", "dispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "preferencesStore", "Lcom/d9tilov/android/datastore/PreferencesStore;", "provideTransactionRepo", "Lcom/d9tilov/android/transaction/domain/contract/TransactionRepo;", "transactionSource", "transaction-di_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.android.components.ActivityRetainedComponent.class})
public final class TransactionDataModule {
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.transaction.di.TransactionDataModule INSTANCE = null;
    
    private TransactionDataModule() {
        super();
    }
    
    @dagger.Provides
    @dagger.hilt.android.scopes.ActivityRetainedScoped
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.database.dao.TransactionDao provideTransactionDao(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.AppDatabase appDatabase) {
        return null;
    }
    
    @dagger.Provides
    @dagger.hilt.android.scopes.ActivityRetainedScoped
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.transaction.data.contract.TransactionSource provideTransactionLocalSource(@com.d9tilov.android.network.dispatchers.Dispatcher(moneyManagerDispatcher = com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers.IO)
    @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineDispatcher dispatcher, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.datastore.PreferencesStore preferencesStore, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.AppDatabase appDatabase) {
        return null;
    }
    
    @dagger.Provides
    @dagger.hilt.android.scopes.ActivityRetainedScoped
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.transaction.domain.contract.TransactionRepo provideTransactionRepo(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.transaction.data.contract.TransactionSource transactionSource) {
        return null;
    }
}