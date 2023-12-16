package com.d9tilov.android.database.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\bg\u0018\u00002\u00020\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006J!\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJ$\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000f0\u000e2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u0011H\'J#\u0010\u0012\u001a\u0004\u0018\u00010\u00052\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0003H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJ$\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u000f0\u000e2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0003H\'J!\u0010\u0014\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\nH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016J\u0019\u0010\u0017\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0018"}, d2 = {"Lcom/d9tilov/android/database/dao/CategoryDao;", "", "create", "", "category", "Lcom/d9tilov/android/database/entity/CategoryDbModel;", "(Lcom/d9tilov/android/database/entity/CategoryDbModel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "", "uid", "", "id", "(Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllByType", "Lkotlinx/coroutines/flow/Flow;", "", "type", "", "getById", "getByParentId", "getCategoriesCountByName", "name", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "database_debug"})
@androidx.room.Dao
public abstract interface CategoryDao {
    
    @androidx.room.Query(value = "SELECT * FROM categories WHERE clientId=:uid AND type=:type")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.database.entity.CategoryDbModel>> getAllByType(@org.jetbrains.annotations.NotNull
    java.lang.String uid, int type);
    
    @androidx.room.Query(value = "SELECT * FROM categories WHERE clientId=:uid AND id=:id")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getById(@org.jetbrains.annotations.NotNull
    java.lang.String uid, long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.d9tilov.android.database.entity.CategoryDbModel> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM categories WHERE clientId=:uid AND name=:name")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object getCategoriesCountByName(@org.jetbrains.annotations.NotNull
    java.lang.String uid, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM categories WHERE clientId=:uid AND parentId=:id")
    @org.jetbrains.annotations.NotNull
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.d9tilov.android.database.entity.CategoryDbModel>> getByParentId(@org.jetbrains.annotations.NotNull
    java.lang.String uid, long id);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object create(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.entity.CategoryDbModel category, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull
    com.d9tilov.android.database.entity.CategoryDbModel category, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM categories WHERE clientId=:uid AND id=:id")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull
    java.lang.String uid, long id, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}