package com.d9tilov.android.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u000e\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J!\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ!\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ4\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000e2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u000fH\'J4\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u00170\u00162\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u000fH\'J\'\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00100\u00172\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\nJ4\u0010\u001a\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u00170\u00162\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\'J\u001e\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00100\u00162\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\tH\'J!\u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u001d\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001eJ1\u0010\u001f\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u00052\u0006\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010 J\u0019\u0010!\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#J\u0019\u0010$\u001a\u00020\u00032\u0006\u0010\"\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010#\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006%"}, d2 = {"Lcom/d9tilov/android/database/dao/TransactionDao;", "", "clearAll", "", "clientId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "id", "", "(Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteByCategoryId", "categoryId", "getAllByType", "Landroidx/paging/PagingSource;", "", "Lcom/d9tilov/android/database/entity/TransactionDbModel;", "from", "Lkotlinx/datetime/LocalDateTime;", "to", "type", "getAllByTypeInPeriod", "Lkotlinx/coroutines/flow/Flow;", "", "getByCategoryId", "uid", "getByCategoryIdInPeriod", "getById", "getCountByCurrencyCode", "code", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getItemsCountInDay", "(Ljava/lang/String;ILkotlinx/datetime/LocalDateTime;Lkotlinx/datetime/LocalDateTime;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "transaction", "(Lcom/d9tilov/android/database/entity/TransactionDbModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "database_debug"})
@androidx.room.Dao
public abstract interface TransactionDao {
    
    @androidx.room.Query(value = "SELECT * FROM transactions WHERE clientId=:clientId AND type = :type AND date >= :from AND date <= :to ORDER BY date DESC")
    @org.jetbrains.annotations.NotNull
    public abstract androidx.paging.PagingSource<java.lang.Integer, com.d9tilov.android.database.entity.TransactionDbModel> getAllByType(@org.jetbrains.annotations.NotNull
    java.lang.String clientId, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime from, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime to, int type);
    
    @androidx.room.Query(value = "SELECT * FROM transactions WHERE clientId=:clientId AND type = :type AND date >= :from AND date <= :to")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.database.entity.TransactionDbModel>> getAllByTypeInPeriod(@org.jetbrains.annotations.NotNull
    java.lang.String clientId, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime from, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime to, int type);
    
    @androidx.room.Query(value = "SELECT * FROM transactions WHERE clientId =:uid AND id = :id")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<com.d9tilov.android.database.entity.TransactionDbModel> getById(@org.jetbrains.annotations.NotNull
    java.lang.String uid, long id);
    
    @androidx.room.Query(value = "SELECT * FROM transactions WHERE clientId =:uid AND categoryId =:categoryId")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getByCategoryId(@org.jetbrains.annotations.NotNull
    java.lang.String uid, long categoryId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.d9tilov.android.database.entity.TransactionDbModel>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM transactions WHERE clientId =:uid AND categoryId =:categoryId AND date >= :from AND date <= :to")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.database.entity.TransactionDbModel>> getByCategoryIdInPeriod(@org.jetbrains.annotations.NotNull
    java.lang.String uid, long categoryId, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime from, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime to);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.entity.TransactionDbModel transaction, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT count(*) FROM transactions WHERE clientId=:uid AND type=:type AND date >= :from AND date <= :to")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getItemsCountInDay(@org.jetbrains.annotations.NotNull
    java.lang.String uid, int type, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime from, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime to, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT count(*) FROM transactions WHERE clientId=:uid AND currency=:code")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getCountByCurrencyCode(@org.jetbrains.annotations.NotNull
    java.lang.String uid, @org.jetbrains.annotations.NotNull
    java.lang.String code, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Update
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.entity.TransactionDbModel transaction, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM transactions WHERE clientId=:clientId AND id=:id")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull
    java.lang.String clientId, long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM transactions WHERE rowId in(SELECT rowId from transactions WHERE clientId=:clientId AND categoryId=:categoryId LIMIT 1)")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteByCategoryId(@org.jetbrains.annotations.NotNull
    java.lang.String clientId, long categoryId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM transactions WHERE clientId=:clientId")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object clearAll(@org.jetbrains.annotations.NotNull
    java.lang.String clientId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}