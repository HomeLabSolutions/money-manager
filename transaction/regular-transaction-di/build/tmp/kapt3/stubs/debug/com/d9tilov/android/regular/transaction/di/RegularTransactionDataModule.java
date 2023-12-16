package com.d9tilov.android.regular.transaction.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\"\u0010\u0007\u001a\u00020\u00062\b\b\u0001\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0007\u00a8\u0006\u000e"}, d2 = {"Lcom/d9tilov/android/regular/transaction/di/RegularTransactionDataModule;", "", "()V", "provideRegularTransactionRepo", "Lcom/d9tilov/android/regular/transaction/domain/contract/RegularTransactionRepo;", "regularTransactionSource", "Lcom/d9tilov/android/regular/transaction/data/contract/RegularTransactionSource;", "provideRegularTransactionSource", "dispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "preferencesStore", "Lcom/d9tilov/android/datastore/PreferencesStore;", "appDatabase", "Lcom/d9tilov/android/database/AppDatabase;", "regular-transaction-di_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.android.components.ActivityRetainedComponent.class})
public final class RegularTransactionDataModule {
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.regular.transaction.di.RegularTransactionDataModule INSTANCE = null;
    
    private RegularTransactionDataModule() {
        super();
    }
    
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.regular.transaction.data.contract.RegularTransactionSource provideRegularTransactionSource(@com.d9tilov.android.network.dispatchers.Dispatcher(moneyManagerDispatcher = com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers.IO)
    @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineDispatcher dispatcher, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.datastore.PreferencesStore preferencesStore, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.AppDatabase appDatabase) {
        return null;
    }
    
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.regular.transaction.domain.contract.RegularTransactionRepo provideRegularTransactionRepo(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.regular.transaction.data.contract.RegularTransactionSource regularTransactionSource) {
        return null;
    }
}