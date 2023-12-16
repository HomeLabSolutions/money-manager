package com.d9tilov.android.currency.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J \u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\rH\u0007J\"\u0010\u000e\u001a\u00020\n2\b\b\u0001\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\f\u001a\u00020\rH\u0007\u00a8\u0006\u0013"}, d2 = {"Lcom/d9tilov/android/currency/di/CurrencyDataModule;", "", "()V", "provideCurrencyApi", "Lcom/d9tilov/android/network/CurrencyApi;", "retrofit", "Lretrofit2/Retrofit;", "provideCurrencyRepo", "Lcom/d9tilov/android/currency/domain/contract/CurrencyRepo;", "currencySource", "Lcom/d9tilov/android/currency/data/contract/CurrencySource;", "currencyApi", "preferencesStore", "Lcom/d9tilov/android/datastore/PreferencesStore;", "provideCurrencySource", "dispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "appDatabase", "Lcom/d9tilov/android/database/AppDatabase;", "currency-di_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class CurrencyDataModule {
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.currency.di.CurrencyDataModule INSTANCE = null;
    
    private CurrencyDataModule() {
        super();
    }
    
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.currency.data.contract.CurrencySource provideCurrencySource(@com.d9tilov.android.network.dispatchers.Dispatcher(moneyManagerDispatcher = com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers.IO)
    @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineDispatcher dispatcher, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.AppDatabase appDatabase, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.datastore.PreferencesStore preferencesStore) {
        return null;
    }
    
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.network.CurrencyApi provideCurrencyApi(@org.jetbrains.annotations.NotNull
    retrofit2.Retrofit retrofit) {
        return null;
    }
    
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.currency.domain.contract.CurrencyRepo provideCurrencyRepo(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.currency.data.contract.CurrencySource currencySource, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.network.CurrencyApi currencyApi, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.datastore.PreferencesStore preferencesStore) {
        return null;
    }
}