package com.d9tilov.android.database.entity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\b\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001b\u001a\u00020\bH\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u000bH\u00c6\u0003JE\u0010\u001d\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u00c6\u0001J\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010!\u001a\u00020\"H\u00d6\u0001J\t\u0010#\u001a\u00020\u0005H\u00d6\u0001R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0016\u0010\n\u001a\u00020\u000b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0016\u0010\u0006\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000eR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0016\u0010\t\u001a\u00020\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0016\u0010\u0007\u001a\u00020\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0015\u00a8\u0006$"}, d2 = {"Lcom/d9tilov/android/database/entity/BudgetDbModel;", "", "id", "", "clientId", "", "currency", "sum", "Ljava/math/BigDecimal;", "saveSum", "createdDate", "Lkotlinx/datetime/LocalDateTime;", "(JLjava/lang/String;Ljava/lang/String;Ljava/math/BigDecimal;Ljava/math/BigDecimal;Lkotlinx/datetime/LocalDateTime;)V", "getClientId", "()Ljava/lang/String;", "getCreatedDate", "()Lkotlinx/datetime/LocalDateTime;", "getCurrency", "getId", "()J", "getSaveSum", "()Ljava/math/BigDecimal;", "getSum", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "", "toString", "database_debug"})
@androidx.room.Entity(tableName = "budget", foreignKeys = {@androidx.room.ForeignKey(entity = com.d9tilov.android.database.entity.UserDbModel.class, parentColumns = {"uid"}, childColumns = {"clientId"}, onDelete = 5)})
public final class BudgetDbModel {
    @androidx.room.PrimaryKey(autoGenerate = true)
    @androidx.room.ColumnInfo(name = "id")
    private final long id = 0L;
    @androidx.room.ColumnInfo(name = "clientId", index = true)
    @org.jetbrains.annotations.NotNull
    private final java.lang.String clientId = null;
    @androidx.room.ColumnInfo(name = "currency")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String currency = null;
    @androidx.room.ColumnInfo(name = "sum")
    @org.jetbrains.annotations.NotNull
    private final java.math.BigDecimal sum = null;
    @androidx.room.ColumnInfo(name = "saveSum")
    @org.jetbrains.annotations.NotNull
    private final java.math.BigDecimal saveSum = null;
    @androidx.room.ColumnInfo(name = "createdDate")
    @org.jetbrains.annotations.NotNull
    private final kotlinx.datetime.LocalDateTime createdDate = null;
    
    public BudgetDbModel(long id, @org.jetbrains.annotations.NotNull
    java.lang.String clientId, @org.jetbrains.annotations.NotNull
    java.lang.String currency, @org.jetbrains.annotations.NotNull
    java.math.BigDecimal sum, @org.jetbrains.annotations.NotNull
    java.math.BigDecimal saveSum, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime createdDate) {
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
    public final java.math.BigDecimal getSum() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.math.BigDecimal getSaveSum() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.datetime.LocalDateTime getCreatedDate() {
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
    public final java.math.BigDecimal component4() {
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
    public final com.d9tilov.android.database.entity.BudgetDbModel copy(long id, @org.jetbrains.annotations.NotNull
    java.lang.String clientId, @org.jetbrains.annotations.NotNull
    java.lang.String currency, @org.jetbrains.annotations.NotNull
    java.math.BigDecimal sum, @org.jetbrains.annotations.NotNull
    java.math.BigDecimal saveSum, @org.jetbrains.annotations.NotNull
    kotlinx.datetime.LocalDateTime createdDate) {
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