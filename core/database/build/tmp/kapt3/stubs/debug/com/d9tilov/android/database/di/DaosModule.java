package com.d9tilov.android.database.di;

@dagger.Module
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u0007"}, d2 = {"Lcom/d9tilov/android/database/di/DaosModule;", "", "()V", "providesCategoryDao", "Lcom/d9tilov/android/database/dao/CategoryDao;", "database", "Lcom/d9tilov/android/database/AppDatabase;", "database_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class DaosModule {
    @org.jetbrains.annotations.NotNull
    public static final com.d9tilov.android.database.di.DaosModule INSTANCE = null;
    
    private DaosModule() {
        super();
    }
    
    @dagger.Provides
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.database.dao.CategoryDao providesCategoryDao(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.AppDatabase database) {
        return null;
    }
}