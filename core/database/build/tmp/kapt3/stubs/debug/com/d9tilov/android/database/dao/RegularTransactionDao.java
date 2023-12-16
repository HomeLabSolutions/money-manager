package com.d9tilov.android.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J$\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\t0\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\'J\'\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00050\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J!\u0010\u0012\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u0010H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011J\u0019\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0019\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0016"}, d2 = {"Lcom/d9tilov/android/database/dao/RegularTransactionDao;", "", "delete", "", "budget", "Lcom/d9tilov/android/database/entity/RegularTransactionDbModel;", "(Lcom/d9tilov/android/database/entity/RegularTransactionDbModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "Lkotlinx/coroutines/flow/Flow;", "", "uid", "", "transactionType", "Lcom/d9tilov/android/core/model/TransactionType;", "getByCategoryId", "categoryId", "", "(Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getById", "id", "insert", "update", "database_debug"})
@androidx.room.Dao
public abstract interface RegularTransactionDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.entity.RegularTransactionDbModel budget, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM regularTransaction WHERE clientId=:uid AND type=:transactionType")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.database.entity.RegularTransactionDbModel>> getAll(@org.jetbrains.annotations.NotNull
    java.lang.String uid, @org.jetbrains.annotations.NotNull
    com.d9tilov.android.core.model.TransactionType transactionType);
    
    @androidx.room.Query(value = "SELECT * FROM regularTransaction WHERE clientId=:uid AND id=:id")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getById(@org.jetbrains.annotations.NotNull
    java.lang.String uid, long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.d9tilov.android.database.entity.RegularTransactionDbModel> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM regularTransaction WHERE clientId =:uid AND categoryId =:categoryId")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getByCategoryId(@org.jetbrains.annotations.NotNull
    java.lang.String uid, long categoryId, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.util.List<com.d9tilov.android.database.entity.RegularTransactionDbModel>> $completion);
    
    @androidx.room.Update
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.entity.RegularTransactionDbModel budget, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.entity.RegularTransactionDbModel budget, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}