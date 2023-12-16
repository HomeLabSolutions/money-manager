package com.d9tilov.android.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\'J\u0010\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\bH\'J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u0007\u001a\u00020\bH\'J\u001f\u0010\u000b\u001a\u00020\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eJ\u0019\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0012"}, d2 = {"Lcom/d9tilov/android/database/dao/CurrencyListDao;", "", "getAll", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/d9tilov/android/database/entity/CurrencyDbModel;", "getByCode", "code", "", "getLastUpdateTime", "", "insert", "", "list", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "currency", "(Lcom/d9tilov/android/database/entity/CurrencyDbModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "database_debug"})
@androidx.room.Dao
public abstract interface CurrencyListDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull
    java.util.List<com.d9tilov.android.database.entity.CurrencyDbModel> list, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.entity.CurrencyDbModel currency, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM currency")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.database.entity.CurrencyDbModel>> getAll();
    
    @androidx.room.Query(value = "SELECT * FROM currency WHERE id=:code")
    @org.jetbrains.annotations.NotNull
    public abstract com.d9tilov.android.database.entity.CurrencyDbModel getByCode(@org.jetbrains.annotations.NotNull
    java.lang.String code);
    
    @androidx.room.Query(value = "SELECT lastUpdateTime FROM currency WHERE id=:code")
    public abstract long getLastUpdateTime(@org.jetbrains.annotations.NotNull
    java.lang.String code);
}