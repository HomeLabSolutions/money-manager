package com.d9tilov.android.database.entity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B=\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\rJ\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\tH\u00c6\u0003J\t\u0010\u001e\u001a\u00020\u000bH\u00c6\u0003J\t\u0010\u001f\u001a\u00020\u0005H\u00c6\u0003JO\u0010 \u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00052\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010$\u001a\u00020%H\u00d6\u0001J\t\u0010&\u001a\u00020\u0005H\u00d6\u0001R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0016\u0010\n\u001a\u00020\u000b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0016\u0010\u0006\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u000fR\u0016\u0010\f\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000fR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0016\u0010\u0007\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u000fR\u0016\u0010\b\u001a\u00020\t8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018\u00a8\u0006\'"}, d2 = {"Lcom/d9tilov/android/database/entity/GoalDbModel;", "", "id", "", "clientId", "", "currency", "name", "targetSum", "Ljava/math/BigDecimal;", "createdDate", "Lkotlinx/datetime/LocalDateTime;", "description", "(JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/math/BigDecimal;Lkotlinx/datetime/LocalDateTime;Ljava/lang/String;)V", "getClientId", "()Ljava/lang/String;", "getCreatedDate", "()Lkotlinx/datetime/LocalDateTime;", "getCurrency", "getDescription", "getId", "()J", "getName", "getTargetSum", "()Ljava/math/BigDecimal;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "", "other", "hashCode", "", "toString", "database_debug"})
@androidx.room.Entity(tableName = "goal", foreignKeys = {@androidx.room.ForeignKey(entity = com.d9tilov.android.database.entity.UserDbModel.class, parentColumns = {"uid"}, childColumns = {"clientId"}, onDelete = 5)})
public final class GoalDbModel {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @androidx.room.ColumnInfo(name = "id")
    private final long id = 0L;
    @androidx.room.ColumnInfo(name = "clientId", index = true)
    @org.jetbrains.annotations.NotNull
    private final java.lang.String clientId = null;
    @androidx.room.ColumnInfo(name = "currency")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String currency = null;
    @androidx.room.ColumnInfo(name = "name")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String name = null;
    @androidx.room.ColumnInfo(name = "targetSum")
    @org.jetbrains.annotations.NotNull
    private final java.math.BigDecimal targetSum = null;
    @androidx.room.ColumnInfo(name = "createdDate")
    @org.jetbrains.annotations.NotNull
    private final kotlinx.datetime.LocalDateTime createdDate = null;
    @androidx.room.ColumnInfo(name = "description")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String description = null;
    
    public GoalDbModel(long id, @org.jetbrains.annotations.NotNull
    java.lang.String clientId, @org.jetbrains.annotations.NotNull
    java.lang.String currency, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.math.BigDecimal targetSum, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime createdDate, @org.jetbrains.annotations.NotNull
    java.lang.String description) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getClientId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getCurrency() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.math.BigDecimal getTargetSum() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.datetime.LocalDateTime getCreatedDate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getDescription() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.math.BigDecimal component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.datetime.LocalDateTime component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.database.entity.GoalDbModel copy(long id, @org.jetbrains.annotations.NotNull
    java.lang.String clientId, @org.jetbrains.annotations.NotNull
    java.lang.String currency, @org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.math.BigDecimal targetSum, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime createdDate, @org.jetbrains.annotations.NotNull
    java.lang.String description) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
}