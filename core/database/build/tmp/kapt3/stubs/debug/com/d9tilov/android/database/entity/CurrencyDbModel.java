package com.d9tilov.android.database.entity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0011\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0012\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0013\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0014\u001a\u00020\bH\u00c6\u0003J1\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\bH\u00c6\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001J\t\u0010\u001b\u001a\u00020\u0003H\u00d6\u0001R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0007\u001a\u00020\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0016\u0010\u0004\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0016\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u001c"}, d2 = {"Lcom/d9tilov/android/database/entity/CurrencyDbModel;", "", "id", "", "symbol", "value", "Ljava/math/BigDecimal;", "lastUpdateTime", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/math/BigDecimal;J)V", "getId", "()Ljava/lang/String;", "getLastUpdateTime", "()J", "getSymbol", "getValue", "()Ljava/math/BigDecimal;", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "", "toString", "database_debug"})
@androidx.room.Entity(tableName = "currency")
public final class CurrencyDbModel {
    @androidx.room.PrimaryKey
    @androidx.room.ColumnInfo(name = "id")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String id = null;
    @androidx.room.ColumnInfo(name = "symbol")
    @org.jetbrains.annotations.NotNull
    private final java.lang.String symbol = null;
    @androidx.room.ColumnInfo(name = "value")
    @org.jetbrains.annotations.NotNull
    private final java.math.BigDecimal value = null;
    @androidx.room.ColumnInfo(name = "lastUpdateTime")
    private final long lastUpdateTime = 0L;
    
    public CurrencyDbModel(@org.jetbrains.annotations.NotNull
    java.lang.String id, @org.jetbrains.annotations.NotNull
    java.lang.String symbol, @org.jetbrains.annotations.NotNull
    java.math.BigDecimal value, long lastUpdateTime) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getSymbol() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.math.BigDecimal getValue() {
        return null;
    }
    
    public final long getLastUpdateTime() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.math.BigDecimal component3() {
        return null;
    }
    
    public final long component4() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.d9tilov.android.database.entity.CurrencyDbModel copy(@org.jetbrains.annotations.NotNull
    java.lang.String id, @org.jetbrains.annotations.NotNull
    java.lang.String symbol, @org.jetbrains.annotations.NotNull
    java.math.BigDecimal value, long lastUpdateTime) {
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