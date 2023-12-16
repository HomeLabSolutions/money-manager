package com.d9tilov.android.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\b2\u0006\u0010\n\u001a\u00020\u0005H\'J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\n\u001a\u00020\u0005H\'J\u0019\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ\u0019\u0010\u0010\u001a\u00020\u00112\u0006\u0010\n\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J\u0019\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\tH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000f\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0013"}, d2 = {"Lcom/d9tilov/android/database/dao/UserDao;", "", "deleteUser", "", "uid", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getById", "Lkotlinx/coroutines/flow/Flow;", "Lcom/d9tilov/android/database/entity/UserDbModel;", "id", "getFiscalDay", "", "insert", "user", "(Lcom/d9tilov/android/database/entity/UserDbModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "showPrepopulate", "", "update", "database_debug"})
@androidx.room.Dao
public abstract interface UserDao {
    
    @androidx.room.Query(value = "SELECT * FROM Users WHERE uid = :id")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<com.d9tilov.android.database.entity.UserDbModel> getById(@org.jetbrains.annotations.NotNull
    java.lang.String id);
    
    @androidx.room.Query(value = "SELECT fiscalDay FROM Users WHERE uid = :id")
    public abstract int getFiscalDay(@org.jetbrains.annotations.NotNull
    java.lang.String id);
    
    @androidx.room.Query(value = "SELECT showPrepopulate FROM Users WHERE uid = :id")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object showPrepopulate(@org.jetbrains.annotations.NotNull
    java.lang.String id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.entity.UserDbModel user, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.entity.UserDbModel user, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM users WHERE uid=:uid")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object deleteUser(@org.jetbrains.annotations.NotNull
    java.lang.String uid, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}