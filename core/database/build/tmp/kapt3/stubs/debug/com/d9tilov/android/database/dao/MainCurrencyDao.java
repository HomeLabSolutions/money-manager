package com.d9tilov.android.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\'J\u0019\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0004H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\n\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u000b"}, d2 = {"Lcom/d9tilov/android/database/dao/MainCurrencyDao;", "", "get", "Lkotlinx/coroutines/flow/Flow;", "Lcom/d9tilov/android/database/entity/MainCurrencyDbModel;", "uid", "", "insert", "", "mainCurrencyDbModel", "(Lcom/d9tilov/android/database/entity/MainCurrencyDbModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "database_debug"})
@androidx.room.Dao
public abstract interface MainCurrencyDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.entity.MainCurrencyDbModel mainCurrencyDbModel, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM main_currency WHERE clientId=:uid")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<com.d9tilov.android.database.entity.MainCurrencyDbModel> get(@org.jetbrains.annotations.NotNull
    java.lang.String uid);
}