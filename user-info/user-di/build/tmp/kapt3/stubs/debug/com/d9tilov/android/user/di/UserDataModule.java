package com.d9tilov.android.user.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\"\u0010\u0007\u001a\u00020\u00062\b\b\u0001\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0007\u00a8\u0006\u000e"}, d2 = {"Lcom/d9tilov/android/user/di/UserDataModule;", "", "()V", "provideUserRepo", "Lcom/d9tilov/android/user/domain/contract/UserRepo;", "userSource", "Lcom/d9tilov/android/user/data/contract/UserSource;", "provideUserSource", "dispatcher", "Lkotlinx/coroutines/CoroutineDispatcher;", "appDatabase", "Lcom/d9tilov/android/database/AppDatabase;", "preferencesStore", "Lcom/d9tilov/android/datastore/PreferencesStore;", "user-di_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class UserDataModule {
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.user.di.UserDataModule INSTANCE = null;
    
    private UserDataModule() {
        super();
    }
    
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.user.data.contract.UserSource provideUserSource(@com.d9tilov.android.network.dispatchers.Dispatcher(moneyManagerDispatcher = com.d9tilov.android.network.dispatchers.MoneyManagerDispatchers.IO)
    @org.jetbrains.annotations.NotNull
    kotlinx.coroutines.CoroutineDispatcher dispatcher, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.AppDatabase appDatabase, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.datastore.PreferencesStore preferencesStore) {
        return null;
    }
    
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.user.domain.contract.UserRepo provideUserRepo(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.user.data.contract.UserSource userSource) {
        return null;
    }
}